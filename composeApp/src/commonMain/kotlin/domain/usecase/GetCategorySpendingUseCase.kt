package domain.usecase

import di.DispatcherProvider
import domain.model.CategorySpending
import domain.model.DateRange
import domain.model.ExpenseIncomeData
import domain.plusMonths
import domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class GetCategorySpendingUseCase(
    private val transactionRepository: TransactionRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun getCategorySpendingDataForChart(
        dateRange: DateRange,
        categoryList: List<String>,
        coroutineScope: CoroutineScope
    ): List<CategorySpending> {
        return coroutineScope.async(dispatcherProvider.io) {
            val list = mutableListOf<CategorySpending>()
            val numMonths = dateRange.getMonthsBetween()
            categoryList.forEach { category ->
                var totalExpenseSum = 0.0
                var totalIncome = 0.0
                val expenseIncomeDataList = 0.rangeTo(numMonths).map { months ->
                    coroutineScope.async(dispatcherProvider.io) {
                        listOf(
                            transactionRepository.getExpenseSumByCategory(
                                startDate = dateRange.startDate.plusMonths(months),
                                endDate = dateRange.startDate.plusMonths(months + 1),
                                category = category
                            ),
                            transactionRepository.getIncomeSumByCategory(
                                startDate = dateRange.startDate.plusMonths(months),
                                endDate = dateRange.startDate.plusMonths(months + 1),
                                category = category
                            )
                        )
                    }
                }.awaitAll().mapIndexed { index, sum ->
                    totalExpenseSum += sum[0]
                    totalIncome += sum[1]
                    ExpenseIncomeData(
                        date = dateRange.startDate.plusMonths(index),
                        expenseAmount = sum[0].toFloat(),
                        incomeAmount = sum[1].toFloat()
                    )
                }.reversed()
                list.add(
                    CategorySpending(
                        categoryName = category,
                        expenseIncomeData = expenseIncomeDataList,
                        totalExpenseSum = totalExpenseSum.toFloat(),
                        totalIncomeSum = totalIncome.toFloat()
                    )
                )
            }
            list.filter { it.totalIncomeSum > 0 || it.totalExpenseSum > 0 }
                .sortedByDescending { it.categoryName }.reversed()
        }.await()
    }
}