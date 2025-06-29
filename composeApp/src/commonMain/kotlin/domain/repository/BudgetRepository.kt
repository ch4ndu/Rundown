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

package domain.repository

import domain.usecase.BudgetUseCase
import data.database.dao.BudgetDao
import data.database.model.Budget
import data.network.firefly.BudgetService
import di.DispatcherProvider
import domain.model.BudgetSpending
import domain.model.DateRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import org.lighthousegames.logging.logging

class BudgetRepository(
    private val budgetDao: BudgetDao,
    private val budgetService: BudgetService,
    private val budgetUseCase: BudgetUseCase,
    private val dispatcherProvider: DispatcherProvider,
) {
    private val log = logging()

    suspend fun loadAllBudgets() {
        withContext(dispatcherProvider.default) {
            var loadData = true
            var pageKey = 1
            while (loadData) {
                val networkCall = budgetService.getAllBudgets(page = pageKey)
                val responseBody = networkCall.body()
                if (networkCall.isSuccessful && responseBody != null) {
//                networkCall.onSuccess { it ->
                    val budgetList = mutableListOf<Budget>()
                    for (budgetData in responseBody.data) {
                        budgetList.add(Budget.fromBudgetData(budgetData))
                    }
                    budgetDao.insertAll(budgetList)
                    if (
                        responseBody.meta.pagination.total_pages !=
                        responseBody.meta.pagination.current_page
                    ) {
                        pageKey += 1
                    } else {
                        loadData = false
                    }
                } else {
//                    .onFailure {
                    log.e { "failed to load budgets: $responseBody" }
                    loadData = false
                }
            }
        }
    }

    suspend fun getAllBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllBudgets()
    }

    suspend fun getBudgetsOverview(
        dateRange: DateRange,
        coroutineScope: CoroutineScope
    ): Flow<List<BudgetSpending>> {
        return budgetDao.getAllBudgets().flatMapLatest { budgetList ->
            budgetUseCase.getBudgetSpendingOverview(
                dateRange = dateRange,
                budgetList = budgetList,
                coroutineScope = coroutineScope
            )
        }
    }
}