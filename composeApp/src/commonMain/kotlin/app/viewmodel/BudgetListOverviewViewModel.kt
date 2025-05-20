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