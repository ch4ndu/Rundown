@file:OptIn(ExperimentalCoroutinesApi::class)

package app.viewmodel.mock

import androidx.lifecycle.SavedStateHandle
import app.viewmodel.AccountCashFlowDetailsViewModel
import data.database.model.transaction.FireFlyTransaction
import di.DispatcherProvider
import domain.model.ExpenseIncomeData
import domain.repository.TransactionRepository
import domain.usecase.GetCashFlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest

class MockAccountCashFlowDetailsViewModel(
    private val transactionRepository: TransactionRepository,
    private val getCashFlowUseCase: GetCashFlowUseCase,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : AccountCashFlowDetailsViewModel(
    transactionRepository,
    getCashFlowUseCase,
    dispatcherProvider,
    savedStateHandle
) {

    override val cashFlowData: StateFlow<List<ExpenseIncomeData>>
        get() = dateRangeFlow.mapLatest {
            return@mapLatest MockData.mockExpenseIncomeList
        }.toStateFlow(initial = emptyList())

    override val transactionsFlow: StateFlow<List<FireFlyTransaction>>
        get() = dateRangeFlow.mapLatest {
            return@mapLatest MockData.mockTransactions.shuffled()
        }.toStateFlow(initial = emptyList())
}