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

package domain.usecase

import di.DispatcherProvider
import domain.model.DateRange
import domain.model.ExpenseIncomeData
import domain.plusMonths
import domain.plusMonthsAtBeginning
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class GetCashFlowUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    fun getCashFlowForDateRange(
        dateRange: DateRange
    ): Flow<List<ExpenseIncomeData>> {
        return accountRepository.getAssetAccountListFlow().flatMapLatest { accountIdList ->
            val numMonths = dateRange.getMonthsBetween()
            val cashFlowList = mutableListOf<ExpenseIncomeData>()
            for (index in numMonths.downTo(0)) {
                val startDate = dateRange.startDate.plusMonthsAtBeginning(index)
                val endDate = startDate.withEndOfMonthAtEndOfDay()
                val data = ExpenseIncomeData(
                    expenseAmount = transactionRepository.getExpenseSumForRange(
                        startDate = startDate,
                        endDate = endDate,
                        accountIdList = accountIdList.map { it.id }
                    ).toFloat(),
                    incomeAmount = transactionRepository.getIncomeSumForRange(
                        startDate = startDate,
                        endDate = endDate,
                        accountIdList = accountIdList.map { it.id }
                    ).toFloat(),
                    date = startDate
                )
                cashFlowList.add(data)
            }
            flowOf(cashFlowList)
        }.flowOn(dispatcherProvider.io)
    }

    suspend fun getAccountCashFlowForDateRange(
        dateRange: DateRange,
        accountId: Long
    ): List<ExpenseIncomeData> {
        return withContext(dispatcherProvider.io) {
            val numMonths = dateRange.getMonthsBetween()
            val cashFlowList = mutableListOf<ExpenseIncomeData>()
            for (index in numMonths.downTo(0)) {
                val startDate = dateRange.startDate.plusMonths(index)
                val endDate = startDate.withEndOfMonthAtEndOfDay()
                val expensesForAccount = transactionRepository.getExpenseSumForRange(
                    startDate = startDate,
                    endDate = endDate,
                    accountIdList = listOf(accountId)
                ).toFloat()
                val incomeForAccount = transactionRepository.getIncomeSumForRange(
                    startDate = startDate,
                    endDate = endDate,
                    accountIdList = listOf(accountId)
                ).toFloat()
                val transfersToAccount =
                    transactionRepository.getTransferSumForDestinationWithRange(
                        startDate,
                        endDate,
                        accountId
                    ).toFloat()
                val transfersFromAccount =
                    transactionRepository.getTransferSumForSourceWithRange(
                        startDate,
                        endDate,
                        accountId
                    ).toFloat()
                val data = ExpenseIncomeData(
                    expenseAmount = expensesForAccount + transfersFromAccount,
                    incomeAmount = incomeForAccount + transfersToAccount,
                    date = startDate
                )
                cashFlowList.add(data)
            }
            cashFlowList
        }
    }
}