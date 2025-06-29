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

package app.viewmodel

import androidx.lifecycle.viewModelScope
import app.ui.areDateRangesEquivalent
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.repository.BudgetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

open class BudgetListOverviewViewModel(
    private val budgetRepository: BudgetRepository
) : BaseViewModel() {

    private val dateRangeFlow =
        MutableStateFlow(
            DateRange(
                startDate = currentDate()
                    .minusMonths(11).atBeginningOfMonth(),
                endDate = currentDate(),
            )
        )

    fun setDateRange(dateRange: DateRange) {
        dateRangeFlow.value = dateRange
    }

    open val budgetListOverviewFlow =
        dateRangeFlow
            .distinctUntilChanged(areDateRangesEquivalent)
            .flatMapLatest { dateRange ->
                budgetRepository.getBudgetsOverview(
                    dateRange = dateRange,
                    coroutineScope = viewModelScope
                )
            }.toStateFlow(initial = emptyList())
}