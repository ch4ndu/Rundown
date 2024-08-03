@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package app.viewmodel

import di.DispatcherProvider
import domain.currentDate
import domain.model.ExpenseData
import domain.plusDaysAtBeginning
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetOverallSpendingUseCase
import domain.withStartOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

class DailySpendingDetailsViewModel(
    private val getOverallSpendingUseCase: GetOverallSpendingUseCase,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    private val dailyExpenseDataSelected = MutableStateFlow<ExpenseData?>(null)

    val dailySpendingFlow =
        getOverallSpendingUseCase.getOverallSpendingByDay(currentDate(), 30)

    val transactionsForSelectedExpenseData =
        accountRepository.getAssetAccountListFlow().flatMapLatest { accountList ->
            dailyExpenseDataSelected.filterNotNull().flatMapLatest { expenseData ->
                val start = expenseData.date.withStartOfDay()
                val end = start.plusDaysAtBeginning(1)
                transactionRepository.getExpensesForAccountListFlow(
                    startDate = start,
                    endDate = end,
                    accountIdList = accountList.map { it.id }
                )
            }
        }.flowOn(dispatcherProvider.io)

    fun updateSelectedExpenseData(expenseData: ExpenseData) {
        dailyExpenseDataSelected.value = expenseData
    }
}