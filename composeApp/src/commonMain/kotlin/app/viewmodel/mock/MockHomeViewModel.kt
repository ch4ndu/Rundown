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

import app.viewmodel.HomeViewModel
import di.DispatcherProvider
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetOverallSpendingUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MockHomeViewModel(
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    transactionRepository: TransactionRepository,
    accountRepository: AccountRepository,
    getCashFlowUseCase: GetCashFlowUseCase,
    dispatcherProvider: DispatcherProvider,
    getBalanceChartDataUseCase: GetBalanceChartDataUseCase
) : HomeViewModel(
    getOverallSpendingUseCase,
    transactionRepository,
    accountRepository,
    getCashFlowUseCase,
    dispatcherProvider,
    getBalanceChartDataUseCase
) {

    override val dailySpendingFlow: StateFlow<List<ExpenseData>>
        get() = flow { emit(MockData.mockExpenseList2) }.toStateFlow(initial = emptyList())

    override val cashFlowDetails: StateFlow<List<ExpenseIncomeData>>
        get() = flow { emit(MockData.mockExpenseIncomeList) }.toStateFlow(initial = emptyList())

    override val netWorthLineDataSetFlow: StateFlow<List<ExpenseData>?>
        get() = flow { emit(MockData.mockExpenseList2) }.toStateFlow(initial = emptyList())
}