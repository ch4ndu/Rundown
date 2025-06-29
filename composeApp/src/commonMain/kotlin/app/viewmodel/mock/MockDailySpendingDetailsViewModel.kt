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

package app.viewmodel.mock

import app.viewmodel.DailySpendingDetailsViewModel
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.model.ExpenseData
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetOverallSpendingUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class MockDailySpendingDetailsViewModel(
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    transactionRepository: TransactionRepository,
    accountRepository: AccountRepository,
    dispatcherProvider: DispatcherProvider
) : DailySpendingDetailsViewModel(
    getOverallSpendingUseCase,
    transactionRepository,
    accountRepository,
    dispatcherProvider
) {

    override val dailySpendingFlow: StateFlow<List<ExpenseData>>
        get() = flow { emit(MockData.mockExpenseList2) }.toStateFlow(initial = emptyList())
    override val transactionsForSelectedExpenseData: StateFlow<List<FireFlyTransaction>>
        get() = dailyExpenseDataSelected.mapLatest {
            MockData.mockTransactions.shuffled()
        }.toStateFlow(initial = emptyList())
}