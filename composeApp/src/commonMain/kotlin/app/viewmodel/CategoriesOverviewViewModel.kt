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

@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package app.viewmodel

import androidx.lifecycle.viewModelScope
import app.ui.areDateRangesEquivalent
import data.database.dao.FireFlyTransactionDataDao
import di.DispatcherProvider
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import domain.withEndOfMonthAtEndOfDay
import domain.withStartOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

open class CategoriesOverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val getCategorySpendingUseCase: GetCategorySpendingUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val fireFlyTransactionDataDao: FireFlyTransactionDataDao,
) : BaseViewModel() {

    private val dateRangeFlow =
        MutableStateFlow(
            DateRange(
                startDate = currentDate().minusMonths(11).atBeginningOfMonth().withStartOfDay(),
                endDate = currentDate().withEndOfMonthAtEndOfDay(),
            )
        )

    fun setDateRange(dateRange: DateRange) {
        dateRangeFlow.value = dateRange
    }

    private val categoriesList by lazy {
        dateRangeFlow
            .distinctUntilChanged(areDateRangesEquivalent)
            .flatMapLatest { dateRange ->
                fireFlyTransactionDataDao.getAllCategoriesByDate(
                    startDate = dateRange.startDate,
                    endDate = dateRange.endDate
                ).flowOn(dispatcherProvider.io)
                    .distinctUntilChanged()
                    .map { categoryList ->
                        val categorySet = categoryList.toSet()
                        categorySet.toList()
                    }
            }
    }

    open val allCategoriesSpending by lazy {
        dateRangeFlow.flatMapLatest { range ->
            categoriesList.flatMapLatest { categories ->
                flow {
                    emit(
                        getCategorySpendingUseCase.getCategorySpendingDataForChart(
                            dateRange = range,
                            categoryList = categories,
                            viewModelScope
                        )
                    )
                }
            }
        }.flowOn(dispatcherProvider.default).toStateFlow(initial = emptyList())
    }
}