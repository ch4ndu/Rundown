@file:OptIn(ExperimentalCoroutinesApi::class)

package domain.usecase

import domain.repository.TransactionRepository
import data.database.model.Budget
import di.DispatcherProvider
import domain.model.BudgetSpending
import domain.model.DateRange
import domain.model.ExpenseData
import domain.plusMonths
import domain.withStartOfDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toInstant

class BudgetUseCase(
    private val transactionRepository: TransactionRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun getBudgetSpendingOverview(
        dateRange: DateRange,
        budgetList: List<Budget>,
        coroutineScope: CoroutineScope
    ): Flow<List<BudgetSpending>> {
        return flow {
            val budgetSpendingList = mutableListOf<BudgetSpending>()
            dateRange.startDate.toInstant(TimeZone.currentSystemDefault()).monthsUntil(
                dateRange.endDate.toInstant(TimeZone.currentSystemDefault()),
                TimeZone.currentSystemDefault()
            )
            val numMonths = dateRange.getMonthsBetween()
            budgetList.sortedBy { it.name }.forEach { budget ->
                val budgetSpendingDataList = mutableListOf<ExpenseData>()
                (numMonths.downTo(0)).forEach { index ->
                    val start = dateRange.startDate.plusMonths(index)
                    val end = start.plusMonths(1).withStartOfDay()
                    val expenseSumJob = coroutineScope.async(dispatcherProvider.io) {
                        transactionRepository.getExpenseSumByBudget(
                            startDate = start,
                            endDate = end,
                            budgetId = budget.id
                        )
                    }
                    val totalExpenseSum = expenseSumJob.await()
                    budgetSpendingDataList.add(
                        ExpenseData(
                            expenseAmount = totalExpenseSum.toFloat(),
                            date = start
                        )
                    )
                }
                budgetSpendingList.add(BudgetSpending(budget, budgetSpendingDataList))
            }
            emit(budgetSpendingList)
        }.flowOn(dispatcherProvider.default)
    }
}