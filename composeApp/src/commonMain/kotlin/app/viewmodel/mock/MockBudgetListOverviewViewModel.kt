package app.viewmodel.mock

import app.viewmodel.BudgetListOverviewViewModel
import domain.model.BudgetSpending
import domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MockBudgetListOverviewViewModel(private val budgetRepository: BudgetRepository) :
    BudgetListOverviewViewModel(budgetRepository) {
    override val budgetListOverviewFlow: StateFlow<List<BudgetSpending>>
        get() = flow { emit(MockData.budgetSpendingList) }.toStateFlow(initial = emptyList())
}