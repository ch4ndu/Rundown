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
import data.database.model.transaction.FireFlyTransaction
import data.database.serializers.DateSerializer
import data.enums.ExpenseType
import di.DispatcherProvider
import domain.model.DateRange
import domain.repository.TransactionRepository
import domain.usecase.GetCashFlowUseCase
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.lighthousegames.logging.logging

open class AccountCashFlowDetailsViewModel(
    private val transactionRepository: TransactionRepository,
    private val getCashFlowUseCase: GetCashFlowUseCase,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val log = logging()
//    private val accountIdFlow = savedStateHandle.getStateFlow("account_id", "0").map { it.toLong() }
    private val accountIdFlow = MutableStateFlow(0L)
    private val startDateFlow = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
    private val endDateFlow = MutableStateFlow(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )

    fun setAccountId(accountId: String) {
        accountIdFlow.value = accountId.toLong()
    }
    fun setStartDate(startDate: String) {
        startDateFlow.value = LocalDateTime.parse(startDate, DateSerializer.isoFormat)
    }

    fun setEndDate(endDate: String) {
        endDateFlow.value = LocalDateTime.parse(endDate, DateSerializer.isoFormat)
    }
//    private val startDateFlow =
//        savedStateHandle.getStateFlow("start_millis", "0").map {
//            LocalDateTime.parse(it, DateSerializer.isoFormat)
//        }
//    private val endDateFlow =
//        savedStateHandle.getStateFlow("end_millis", "0").map {
//            LocalDateTime.parse(it, DateSerializer.isoFormat)
//        }

    val dateRangeFlow = combine(startDateFlow, endDateFlow) { startDate, endDate ->
        DateRange(startDate, endDate)
    }

    private val selectedTypeFlow = MutableStateFlow(ExpenseType.EXPENSE)

    private val selectedMonthFlow =
        MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))

    fun updateSelectedMonth(dateTime: LocalDateTime) {
        selectedMonthFlow.value = dateTime
    }

    open val cashFlowData = combine(dateRangeFlow, accountIdFlow) { dateRange, accountId ->
        log.d { "startDate:${dateRange.startDate.format(DateSerializer.chartDayMonthYearFormat)}" }
        log.d { "endDate:${dateRange.endDate.format(DateSerializer.chartDayMonthYearFormat)}" }
        getCashFlowUseCase.getAccountCashFlowForDateRange(dateRange, accountId)
    }.toStateFlow(initial = emptyList())

    open val transactionsFlow = combine(
        selectedTypeFlow,
        selectedMonthFlow,
        accountIdFlow
    ) { selectedType, selectedMonth, accountId ->
        val range = DateRange(
            selectedMonth, selectedMonth.withEndOfMonthAtEndOfDay()
        )

        val list = mutableListOf<FireFlyTransaction>()
        list.addAll(transactionRepository.getCashFlowExpensesForAccount(range, accountId))
        val incomes = transactionRepository.getCashFlowIncomesForAccount(range, accountId)
        list.addAll(incomes)
        list.sortedBy { it.date }.reversed()

//        if (selectedType == ExpenseType.EXPENSE) {
//            transactionRepository.getCashFlowExpensesForAccount(range, accountId)
//        } else {
//            transactionRepository.getCashFlowIncomesForAccount(range, accountId)
//        }
    }.flowOn(dispatcherProvider.io)
        .toStateFlow(initial = emptyList())
}