package app.viewmodel.mock

import androidx.lifecycle.SavedStateHandle
import app.viewmodel.CategoryDetailsViewModel
import data.database.dao.FireFlyTransactionDataDao
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.repository.TransactionRepository
import domain.usecase.GetCategorySpendingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
    override val transactionsForCategory: Flow<List<FireFlyTransaction>>
        get() = flow { MockData.mockTransactions.shuffled() }
}