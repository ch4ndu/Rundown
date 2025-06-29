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

import androidx.lifecycle.SavedStateHandle
import di.DispatcherProvider
import domain.NetworkLoadState
import domain.model.DateRange
import domain.repository.AccountRepository
import domain.usecase.GetAccountSpendingUseCase
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetOverallSpendingUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
open class AccountChartsViewModel(
    private val dispatcherProvider: DispatcherProvider,
    getBalanceChartDataUseCase: GetBalanceChartDataUseCase,
    private val getAccountSpendingUseCase: GetAccountSpendingUseCase,
    private val getOverallSpendingUseCase: GetOverallSpendingUseCase,
    private val getCashFlowUseCase: GetCashFlowUseCase,
    accountRepository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val networkLoadState = MutableStateFlow(NetworkLoadState.Loading)

    private val dateRangeFlow = MutableStateFlow(
        DateRange(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    )

    private val accountIdFlow = MutableStateFlow("0")
    private val accountNameFlow = MutableStateFlow("0")

    fun setAccountName(accountName: String) {
        accountNameFlow.value = accountName
    }

    fun setAccountId(accountId: String) {
        accountIdFlow.value = accountId
    }

    //    private val accountNameFlow = savedStateHandle.getStateFlow("accountName", "0")
//    private val accountIdFlow = savedStateHandle.getStateFlow("accountId", "0")
//    private val accountTypeFlow = savedStateHandle.getStateFlow("accountType", "unknown")

    fun setDateRange(dateRange: DateRange) {
        dateRangeFlow.value = dateRange
    }

    // LineChart data
    open val chartDataFlow = dateRangeFlow.flatMapLatest { dateRange ->
        accountNameFlow.flatMapLatest { accountName ->
            getBalanceChartDataUseCase.accountChartData(
                dateRange = dateRange,
                accountName = accountName,
            )
        }
    }.flowOn(dispatcherProvider.default)
        .toStateFlow(initial = null)

    val lineDataSetFlowVico =
        chartDataFlow.flatMapLatest { chartData ->
            flowOf(chartData?.expenseDataList)
//            getBalanceChartDataUseCase.accountLineChartDataVico(chartData)
        }.toStateFlow(initial = null)

//    val vicoLineFormatterFlow = chartDataFlow.flatMapLatest { chartData ->
//        val entries = chartData?.expenseDataList ?: emptyList()
//        flowOf(VicoLineBottomAxisFormatter(entries))
//    }

    open val spendingDataFlow = combine(dateRangeFlow, accountIdFlow) { dateRange, accountId ->
        getOverallSpendingUseCase.getOverallSpendingForAccountByMonth(dateRange, accountId.toLong())
    }.flowOn(dispatcherProvider.default).toStateFlow(initial = emptyList())

    open val cashFlowData = combine(dateRangeFlow, accountIdFlow) { dateRange, accountId ->
        getCashFlowUseCase.getAccountCashFlowForDateRange(dateRange, accountId.toLong())
    }.flowOn(dispatcherProvider.default).toStateFlow(initial = emptyList())
}