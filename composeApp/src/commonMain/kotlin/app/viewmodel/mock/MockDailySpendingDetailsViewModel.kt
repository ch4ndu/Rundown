package app.viewmodel.mock

import app.viewmodel.DailySpendingDetailsViewModel
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.model.ExpenseData
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetOverallSpendingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class MockDailySpendingDetailsViewModel(
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    transactionRepository: TransactionRepository,
    accountRepository: AccountRepository,
    dispatcherProvider: DispatcherProvider
) : DailySpendingDetailsViewModel(
    getOverallSpendingUseCase,
    transactionRepository,
    accountRepository,
    dispatcherProvider
) {

    override val dailySpendingFlow: Flow<List<ExpenseData>>
        get() = flow { emit(MockData.mockExpenseList2) }
    override val transactionsForSelectedExpenseData: Flow<List<FireFlyTransaction>>
        get() = dailyExpenseDataSelected.mapLatest {
            MockData.mockTransactions.shuffled()
        }
}