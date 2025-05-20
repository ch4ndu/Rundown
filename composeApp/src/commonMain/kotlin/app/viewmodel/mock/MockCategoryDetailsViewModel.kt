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