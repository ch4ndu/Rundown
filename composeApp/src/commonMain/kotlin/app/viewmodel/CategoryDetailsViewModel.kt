/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package app.viewmodel

import androidx.lifecycle.SavedStateHandle
import data.database.dao.FireFlyTransactionDataDao
import data.database.serializers.DateSerializer
import di.DispatcherProvider
import domain.atBeginningOfMonth
import domain.model.DateRange
import domain.model.ExpenseIncomeData
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.LocalDateTime

open class CategoryDetailsViewModel(
    private val transactionRepository: TransactionRepository,
    getCategorySpendingUseCase: GetCategorySpendingUseCase,
    dispatcherProvider: DispatcherProvider,
    fireFlyTransactionDataDao: FireFlyTransactionDataDao,
    savedStateHandle: SavedStateHandle
) : CategoriesOverviewViewModel(
    transactionRepository, getCategorySpendingUseCase, dispatcherProvider, fireFlyTransactionDataDao
) {

    private val spendingDateSelected = MutableStateFlow<ExpenseIncomeData?>(null)

    fun setDateRange(
        startDate: String,
        endDate: String
    ) {
        setDateRange(
            DateRange(
                startDate = LocalDateTime.parse(startDate, DateSerializer.isoFormat),
                endDate = LocalDateTime.parse(endDate, DateSerializer.isoFormat)
            )
        )
    }

    //    val selectedCategory = savedStateHandle.getStateFlow("category", "")
    val selectedCategory = MutableStateFlow("")

    fun setSelectedCategory(category: String) {
        selectedCategory.value = category
    }

    open val categorySpending = selectedCategory.filterNot { it.isEmpty() }.flatMapLatest { category ->
        allCategoriesSpending.flatMapLatest { categorySpendingList ->
            flowOf(
                categorySpendingList.find { it.categoryName == category }
            )
        }
    }.distinctUntilChanged()
        .flowOn(dispatcherProvider.io)
        .toStateFlow(initial = null)

    open val transactionsForCategory =
        spendingDateSelected.filterNotNull().flatMapLatest { spendingData ->
            val startDate = spendingData.date.atBeginningOfMonth()
            val endDate = startDate.withEndOfMonthAtEndOfDay()
            val categoryName = selectedCategory.first()
            transactionRepository.getTransactionsForCategory(
                startDate,
                endDate,
                categoryName
            )
        }.flowOn(dispatcherProvider.io)
            .toStateFlow(initial = emptyList())

    open fun updateSelectedSpendingData(expenseIncomeData: ExpenseIncomeData) {
        spendingDateSelected.value = expenseIncomeData
    }
}