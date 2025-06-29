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
import app.viewmodel.CategoryDetailsViewModel
import data.database.dao.FireFlyTransactionDataDao
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.model.CategorySpending
import domain.model.ExpenseIncomeData
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockCategoryDetailsViewModel(
    private val transactionRepository: TransactionRepository,
    getCategorySpendingUseCase: GetCategorySpendingUseCase,
    dispatcherProvider: DispatcherProvider,
    fireFlyTransactionDataDao: FireFlyTransactionDataDao,
    savedStateHandle: SavedStateHandle
) : CategoryDetailsViewModel(
    transactionRepository,
    getCategorySpendingUseCase,
    dispatcherProvider,
    fireFlyTransactionDataDao,
    savedStateHandle
) {
    override val transactionsForCategory: MutableStateFlow<List<FireFlyTransaction>> =
        MutableStateFlow(MockData.mockTransactions.shuffled())

    override val categorySpending: StateFlow<CategorySpending?> = MutableStateFlow(
        CategorySpending(
            categoryName = selectedCategory.value,
            expenseIncomeData = MockData.mockExpenseIncomeList,
            totalIncomeSum = 500f,
            totalExpenseSum = 1000f
        )
    )

    override fun updateSelectedSpendingData(expenseIncomeData: ExpenseIncomeData) {
        val list = mutableListOf<FireFlyTransaction>()
        list.addAll(MockData.mockTransactions.shuffled())
        transactionsForCategory.value = list
    }
}