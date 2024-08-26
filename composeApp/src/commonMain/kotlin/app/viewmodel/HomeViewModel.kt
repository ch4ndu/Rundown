package app.viewmodel

import di.DispatcherProvider
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.model.ExpenseData
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import domain.usecase.GetBalanceChartDataUseCase
import domain.usecase.GetCashFlowUseCase
import domain.usecase.GetOverallSpendingUseCase
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
open class HomeViewModel(
    private val getOverallSpendingUseCase: GetOverallSpendingUseCase,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val getCashFlowUseCase: GetCashFlowUseCase,
    private val dispatcherProvider: DispatcherProvider,
    getBalanceChartDataUseCase: GetBalanceChartDataUseCase
) : BaseViewModel() {

    private val dailyExpenseDataSelected = MutableStateFlow<ExpenseData?>(null)

    fun updateSelectedExpenseData(expenseData: ExpenseData) {
        dailyExpenseDataSelected.value = expenseData
    }

    val netWorthChartDataFlow =
        accountRepository.getAssetAccountListFlow().flatMapLatest { accountList ->
            getBalanceChartDataUseCase.getCombinedChartData(
                DateRange(
                    currentDate().minusMonths(12).atBeginningOfMonth(),
                    currentDate().withEndOfMonthAtEndOfDay()
                ), accountList
            )
        }.flowOn(dispatcherProvider.io).toStateFlow(initial = null)

    open val dailySpendingFlow =
        getOverallSpendingUseCase.getOverallSpendingByDay(currentDate(), 30)
            .toStateFlow(initial = emptyList())

    val dailySpendingAverage = dailySpendingFlow.map { spendingData ->
        spendingData.map { it.expenseAmount }.average()
    }

    open val cashFlowDetails = getCashFlowUseCase.getCashFlowForDateRange(
        DateRange(
            currentDate().minusMonths(12).atBeginningOfMonth(),
            currentDate().withEndOfMonthAtEndOfDay()
        )
    ).toStateFlow(initial = emptyList())

    val cashFlowAverages = cashFlowDetails.map { cashFlowdata ->
        val expenseAverage = cashFlowdata.map { it.expenseAmount }.average()
        val incomeAverage = cashFlowdata.map { it.incomeAmount }.average()
        hashMapOf(Pair("expense", expenseAverage), Pair("income", incomeAverage))
    }

    open val netWorthLineDataSetFlow =
        netWorthChartDataFlow.flatMapLatest { groupedExpenseData ->
            flowOf(groupedExpenseData?.expenseDataList)
        }.toStateFlow(initial = null)
}