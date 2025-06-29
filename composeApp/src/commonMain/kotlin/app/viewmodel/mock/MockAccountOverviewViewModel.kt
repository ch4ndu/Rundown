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
import app.model.TotalsSummary
import app.viewmodel.AccountOverviewViewModel
import data.database.dao.FireFlyTransactionDataDao
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.repository.TransactionRepository
import domain.usecase.SyncWithServerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

class MockAccountOverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val fireFlyTransactionDataDao: FireFlyTransactionDataDao,
    private val syncWithServerUseCase: SyncWithServerUseCase,
    savedStateHandle: SavedStateHandle
) : AccountOverviewViewModel(
    transactionRepository,
    dispatcherProvider,
    fireFlyTransactionDataDao,
    syncWithServerUseCase,
    savedStateHandle
) {

    override val topTransactionsFlow = flow {
        emit(MockData.mockTransactions)
    }.toStateFlow(initial = emptyList())

    override fun refreshRemoteData() {
        //Do Nothing
    }

    override val tagsForAccountWithDate: Flow<List<String>> = flow {
        emit(
            listOf("Groceries", "HomeExpenses", "Coffee", "Misc")
        )
    }

    override val tagSummaryAccount: StateFlow<HashMap<String, TotalsSummary>> = flow {
        emit(
            hashMapOf(
                Pair(
                    "Groceries",
                    TotalsSummary(expenseSum = 300.0, transferSum = 0.0, incomeSum = 0.0)
                ),
                Pair(
                    "HomeExpenses",
                    TotalsSummary(expenseSum = 1200.0, transferSum = 0.0, incomeSum = 0.0)
                ),
                Pair(
                    "Coffee",
                    TotalsSummary(expenseSum = 300.0, transferSum = 0.0, incomeSum = 0.0)
                ),
                Pair(
                    "Misc",
                    TotalsSummary(expenseSum = 100.0, transferSum = 200.0, incomeSum = 0.0)
                )
            )
        )
    }.toStateFlow(initial = HashMap<String, TotalsSummary>())

    override val transactionsForActiveTag: StateFlow<List<FireFlyTransaction>> = topTransactionsFlow
    override val categoriesForAccountWithDate: StateFlow<List<String>> =
        tagsForAccountWithDate.toStateFlow(initial = emptyList())
    override val categorySummaryAccount: StateFlow<HashMap<String, TotalsSummary>> =
        tagSummaryAccount
    override val transactionsForActiveCategory: StateFlow<List<FireFlyTransaction>> =
        transactionsForActiveTag

    override fun getNewTransactionListForAccount(
        accountType: String,
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): StateFlow<List<FireFlyTransaction>> {
        return transactionsForActiveTag
    }
}