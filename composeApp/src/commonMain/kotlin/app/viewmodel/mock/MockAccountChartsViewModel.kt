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

package app.viewmodel.mock

import androidx.lifecycle.SavedStateHandle
import app.viewmodel.AccountChartsViewModel
import di.DispatcherProvider
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import domain.model.GroupedExpenseData
import domain.repository.AccountRepository
import domain.usecase.GetAccountSpendingUseCase
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetOverallSpendingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MockAccountChartsViewModel(
    val dispatcherProvider: DispatcherProvider,
    getBalanceChartDataUseCase: GetBalanceChartDataUseCase,
    getAccountSpendingUseCase: GetAccountSpendingUseCase,
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    getCashFlowUseCase: GetCashFlowUseCase,
    accountRepository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : AccountChartsViewModel(
    dispatcherProvider,
    getBalanceChartDataUseCase,
    getAccountSpendingUseCase,
    getOverallSpendingUseCase,
    getCashFlowUseCase,
    accountRepository,
    savedStateHandle
) {
    override val chartDataFlow: StateFlow<GroupedExpenseData?>
        get() = flow {
            emit(
                GroupedExpenseData(
                    name = "CheckingAccount",
                    DateRange(startDate = currentDate().minusMonths(6), endDate = currentDate()),
                    expenseDataList = MockData.mockExpenseList2
                )
            )
        }.toStateFlow(initial = null)

    override val spendingDataFlow: StateFlow<List<ExpenseData>>
        get() = flow {
            emit(MockData.mockExpenseList.shuffled())
        }.toStateFlow(initial = emptyList())

    override val cashFlowData: StateFlow<List<ExpenseIncomeData>>
        get() = flow {
            emit(MockData.mockExpenseIncomeList)
        }.toStateFlow(initial = emptyList())
}