package app.viewmodel.mock

import app.viewmodel.HomeViewModel
import di.DispatcherProvider
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetOverallSpendingUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class MockHomeViewModel(
    getOverallSpendingUseCase: GetOverallSpendingUseCase,
    transactionRepository: TransactionRepository,
    accountRepository: AccountRepository,
    getCashFlowUseCase: GetCashFlowUseCase,
    dispatcherProvider: DispatcherProvider,
    getBalanceChartDataUseCase: GetBalanceChartDataUseCase
) : HomeViewModel(
    getOverallSpendingUseCase,
    transactionRepository,
    accountRepository,
    getCashFlowUseCase,
    dispatcherProvider,
    getBalanceChartDataUseCase
) {

    override val dailySpendingFlow: StateFlow<List<ExpenseData>>
        get() = flow { emit(MockData.mockExpenseList2) }.toStateFlow(initial = emptyList())

    override val cashFlowDetails: StateFlow<List<ExpenseIncomeData>>
        get() = flow { emit(MockData.mockExpenseIncomeList) }.toStateFlow(initial = emptyList())

    override val netWorthLineDataSetFlow: StateFlow<List<ExpenseData>?>
        get() = flow { emit(MockData.mockExpenseList2) }.toStateFlow(initial = emptyList())
}