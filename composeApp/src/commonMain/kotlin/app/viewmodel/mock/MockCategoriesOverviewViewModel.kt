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

import app.viewmodel.CategoriesOverviewViewModel
import data.database.dao.FireFlyTransactionDataDao
import di.DispatcherProvider
import domain.model.CategorySpending
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MockCategoriesOverviewViewModel(
    private val transactionRepository: TransactionRepository,
    private val getCategorySpendingUseCase: GetCategorySpendingUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val fireFlyTransactionDataDao: FireFlyTransactionDataDao,
) : CategoriesOverviewViewModel(
    transactionRepository, getCategorySpendingUseCase, dispatcherProvider, fireFlyTransactionDataDao
) {

    override val allCategoriesSpending: StateFlow<List<CategorySpending>>
        get() = flow { emit(MockData.categorySpendingList) }.toStateFlow(initial = emptyList())
}