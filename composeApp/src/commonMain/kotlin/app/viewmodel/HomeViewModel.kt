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

package app.viewmodel

import di.DispatcherProvider
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.model.ExpenseData
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetOverallSpendingUseCase
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
open class HomeViewModel(
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    private val transactionRepository: TransactionRepository,
    accountRepository: AccountRepository,
    getCashFlowUseCase: GetCashFlowUseCase,
    dispatcherProvider: DispatcherProvider,
    getBalanceChartDataUseCase: GetBalanceChartDataUseCase
) : BaseViewModel() {

    private val dailyExpenseDataSelected = MutableStateFlow<ExpenseData?>(null)

    fun updateSelectedExpenseData(expenseData: ExpenseData) {
        dailyExpenseDataSelected.value = expenseData
    }

    val netWorthChartDataFlow =
        accountRepository.getAssetAccountListFlow().flatMapLatest { accountList ->
            getBalanceChartDataUseCase.getCombinedChartData(
                DateRange(
                    currentDate().minusMonths(12).atBeginningOfMonth(),
                    currentDate().withEndOfMonthAtEndOfDay()
                ), accountList
            )
        }.flowOn(dispatcherProvider.io).toStateFlow(initial = null)

    open val dailySpendingFlow =
        getOverallSpendingUseCase.getOverallSpendingByDay(currentDate(), 30)
            .toStateFlow(initial = emptyList())

    val dailySpendingAverage = dailySpendingFlow.map { spendingData ->
        spendingData.map { it.expenseAmount }.average()
    }.toStateFlow(initial = null)

    open val cashFlowDetails = getCashFlowUseCase.getCashFlowForDateRange(
        DateRange(
            currentDate().minusMonths(12).atBeginningOfMonth(),
            currentDate().withEndOfMonthAtEndOfDay()
        )
    ).toStateFlow(initial = emptyList())

    val cashFlowAverages = cashFlowDetails.map { cashFlowdata ->
        val expenseAverage = cashFlowdata.map { it.expenseAmount }.average()
        val incomeAverage = cashFlowdata.map { it.incomeAmount }.average()
        hashMapOf(Pair("expense", expenseAverage), Pair("income", incomeAverage))
    }.toStateFlow(initial = hashMapOf<String, Double>())

    open val netWorthLineDataSetFlow =
        netWorthChartDataFlow.flatMapLatest { groupedExpenseData ->
            flowOf(groupedExpenseData?.expenseDataList)
        }.toStateFlow(initial = null)
}