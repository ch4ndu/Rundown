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