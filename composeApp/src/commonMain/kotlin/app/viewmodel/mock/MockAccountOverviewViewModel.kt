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
    }

    override fun refreshRemoteData() {
        //Do Nothing
    }

    override val tagsForAccountWithDate: Flow<List<String>> = flow {
        emit(
            listOf("Groceries", "HomeExpenses", "Coffee", "Misc")
        )
    }

    override val tagSummaryAccount: Flow<HashMap<String, TotalsSummary>> = flow {
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
    }

    override val transactionsForActiveTag: Flow<List<FireFlyTransaction>> = topTransactionsFlow
    override val categoriesForAccountWithDate: Flow<List<String>> = tagsForAccountWithDate
    override val categorySummaryAccount: Flow<HashMap<String, TotalsSummary>> = tagSummaryAccount
    override val transactionsForActiveCategory: Flow<List<FireFlyTransaction>> =
        transactionsForActiveTag

    override fun getNewTransactionListForAccount(
        accountType: String,
        accountId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<FireFlyTransaction>> {
        return transactionsForActiveTag
    }
}