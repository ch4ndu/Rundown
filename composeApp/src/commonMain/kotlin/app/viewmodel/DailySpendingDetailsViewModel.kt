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

import di.DispatcherProvider
import domain.currentDate
import domain.model.ExpenseData
import domain.plusDaysAtBeginning
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetOverallSpendingUseCase
import domain.withStartOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

open class DailySpendingDetailsViewModel(
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    private val transactionRepository: TransactionRepository,
    accountRepository: AccountRepository,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    open val dailyExpenseDataSelected = MutableStateFlow<ExpenseData?>(null)

    open val dailySpendingFlow =
        getOverallSpendingUseCase.getOverallSpendingByDay(currentDate(), 30)
            .toStateFlow(initial = emptyList())

    open val transactionsForSelectedExpenseData =
        accountRepository.getAssetAccountListFlow().flatMapLatest { accountList ->
            dailyExpenseDataSelected.filterNotNull().flatMapLatest { expenseData ->
                val start = expenseData.date.withStartOfDay()
                val end = start.plusDaysAtBeginning(1)
                transactionRepository.getExpensesForAccountListFlow(
                    startDate = start,
                    endDate = end,
                    accountIdList = accountList.map { it.id }
                )
            }
        }.flowOn(dispatcherProvider.io)
            .toStateFlow(initial = emptyList())

    fun updateSelectedExpenseData(expenseData: ExpenseData) {
        dailyExpenseDataSelected.value = expenseData
    }
}