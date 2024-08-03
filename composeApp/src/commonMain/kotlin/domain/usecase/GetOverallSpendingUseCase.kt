@file:OptIn(ExperimentalCoroutinesApi::class)

package domain.usecase

import di.DispatcherProvider
import domain.minusDaysAtBeginning
import domain.model.DateRange
import domain.model.ExpenseData
import domain.plusDaysAtBeginning
import domain.plusMonths
import domain.repository.AccountRepository
import domain.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class GetOverallSpendingUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    fun getOverallSpendingByDay(
        startDate: LocalDateTime,
        numDays: Int,
    ): Flow<List<ExpenseData>> {
        return accountRepository.getAccountListFlow("asset")
            .flatMapLatest { accountIdList ->
                val expenseDataList = mutableListOf<ExpenseData>()
                (0.rangeUntil(numDays)).forEach { day ->
                    val start = startDate.minusDaysAtBeginning(day)
                    val end = start.plusDaysAtBeginning(1)
                    expenseDataList.add(
                        ExpenseData(
                            expenseAmount = transactionRepository.getExpenseSumForRange(
                                startDate = start,
                                endDate = end,
                                accountIdList = accountIdList.map { it.id }
                            ).toFloat(),
                            date = start
                        )
                    )
                }
                flowOf(expenseDataList)
            }.flowOn(dispatcherProvider.io)
    }

    suspend fun getOverallSpendingForAccountByMonth(
        dateRange: DateRange,
        accountId: Long
    ): List<ExpenseData> {
        return withContext(dispatcherProvider.io) {
            val expenseDataList = mutableListOf<ExpenseData>()
            val numMonths = dateRange.getMonthsBetween()
            if (numMonths == 0) {
                expenseDataList
            }
            (numMonths.downTo(0)).forEach { index ->
                val start = dateRange.startDate.plusMonths(index)
                val end = start.plusMonths(1)
                expenseDataList.add(
                    ExpenseData(
                        expenseAmount = transactionRepository.getExpenseSumForRange(
                            startDate = start,
                            endDate = end,
                            accountIdList = listOf(accountId)
                        ).toFloat(),
                        date = start
                    )
                )
            }
            expenseDataList
        }
    }
}