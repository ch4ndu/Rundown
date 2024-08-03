@file:Suppress("PrivatePropertyName")

package domain.usecase

import data.database.model.insight.InsightData
import di.DispatcherProvider
import domain.NetworkLoadState
import domain.model.DateRange
import domain.model.InsightChartData
import domain.model.SpendingDataForAccount
import domain.plusMonths
import domain.repository.InsightRepository
import domain.withEndOfMonthAtEndOfDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDateTime

class GetAccountSpendingUseCase(
    private val insightRepository: InsightRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    private val TAG = "GetBarChartForAccountUseCase"

    suspend fun getSpendingBreakdownForAccount(
        dateRange: DateRange,
        accountId: String,
        coroutineScope: CoroutineScope,
        networkLoadState: MutableStateFlow<NetworkLoadState>
    ): SpendingDataForAccount {
        networkLoadState.value = NetworkLoadState.Loading
        val numMonths = dateRange.getMonthsBetween() + 1
        val incomeList = getIncomeBreakdownForAccount(
            coroutineScope = coroutineScope,
            accountId = accountId,
            currentDate = dateRange.startDate,
            numMonths = numMonths
        )
        val transferList = getTransferBreakdownForAccount(
            coroutineScope = coroutineScope,
            accountId = accountId,
            currentDate = dateRange.startDate,
            numMonths = numMonths
        )
        val expenseList = getExpenseBreakdownForAccount(
            coroutineScope = coroutineScope,
            accountId = accountId,
            currentDate = dateRange.startDate,
            numMonths = numMonths
        )
        networkLoadState.value = NetworkLoadState.Success
        return SpendingDataForAccount(
            incomeInsight = incomeList,
            expenseInsight = expenseList,
            transferInsight = transferList
        )
    }

    private suspend fun getIncomeBreakdownForAccount(
        coroutineScope: CoroutineScope,
        accountId: String,
        currentDate: LocalDateTime,
        numMonths: Int
    ): List<InsightChartData?> {
        val asyncIncomeList = mutableListOf<Deferred<InsightData?>>()
        val incomeInsightList = mutableListOf<InsightData?>()
        for (i in 0 until numMonths) {
            val currentMonth = currentDate.plusMonths(i)
            asyncIncomeList.add(
                coroutineScope.async(dispatcherProvider.default) {
                    insightRepository.getIncomeInsightForAccount(
                        accountId,
                        startDate = currentMonth,
                        endDate = currentMonth.withEndOfMonthAtEndOfDay()
                    )
                }
            )
        }
        asyncIncomeList.mapIndexed { index, deferred ->
            incomeInsightList.add(index, deferred.await())
        }
//        Log.d(TAG, "income insight Response")
//        for (insight in incomeInsightList) {
//            Log.d(TAG, insight.toString())
//        }
        val insightChartDataList = mutableListOf<InsightChartData?>()
        for (i in 0 until numMonths) {
            val insight = incomeInsightList[i]
            if (insight == null) {
                insightChartDataList.add(null)
                continue
            }
            insightChartDataList.add(
                InsightChartData(
                    insight,
                    date = currentDate.plusMonths(i),
                    insight.difference_float
                )
            )
        }
        return insightChartDataList
    }

    private suspend fun getTransferBreakdownForAccount(
        coroutineScope: CoroutineScope,
        accountId: String,
        currentDate: LocalDateTime,
        numMonths: Int
    ): List<InsightChartData?> {
        val asyncTransferList = mutableListOf<Deferred<InsightData?>>()
        val transferInsightList = mutableListOf<InsightData?>()
        for (i in 0 until numMonths) {
            asyncTransferList.add(
                coroutineScope.async(dispatcherProvider.default) {
                    val currentMonth = currentDate.plusMonths(i)
                    insightRepository.getTransferInsightForAccount(
                        accountId,
                        startDate = currentMonth,
                        endDate = currentMonth.withEndOfMonthAtEndOfDay()
                    )
                }
            )
        }
        asyncTransferList.mapIndexed { index, deferred ->
            transferInsightList.add(index, deferred.await())
        }
//        Log.d(TAG, "transfer insight Response")
//        for (insight in transferInsightList) {
//            Log.d(TAG, insight.toString())
//        }
        val insightChartDataList = mutableListOf<InsightChartData?>()
        for (i in 0 until numMonths) {
            val insight = transferInsightList[i]
            if (insight == null) {
                insightChartDataList.add(null)
                continue
            }
            insightChartDataList.add(
                InsightChartData(
                    insight,
                    date = currentDate.plusMonths(i),
                    insight.difference_float
                )
            )
        }
        return insightChartDataList
    }

    private suspend fun getExpenseBreakdownForAccount(
        coroutineScope: CoroutineScope,
        accountId: String,
        currentDate: LocalDateTime,
        numMonths: Int
    ): List<InsightChartData?> {
        val asyncExpenseList = mutableListOf<Deferred<InsightData?>>()
        val expenseInsightList = mutableListOf<InsightData?>()
        for (i in 0 until numMonths) {
            asyncExpenseList.add(
                coroutineScope.async(dispatcherProvider.default) {
                    val currentMonth = currentDate.plusMonths(i)
                    insightRepository.getExpenseInsightForAccount(
                        accountId,
                        startDate = currentMonth,
                        endDate = currentMonth.withEndOfMonthAtEndOfDay()
                    )
                }
            )
        }
        asyncExpenseList.mapIndexed { index, deferred ->
            expenseInsightList.add(index, deferred.await())
        }
//        Log.d(TAG, "transfer insight Response")
//        for (insight in expenseInsightList) {
//            Log.d(TAG, insight.toString())
//        }
        val insightChartDataList = mutableListOf<InsightChartData?>()
        for (i in 0 until numMonths) {
            val insight = expenseInsightList[i]
            if (insight == null) {
                insightChartDataList.add(null)
                continue
            }
            insightChartDataList.add(
                InsightChartData(
                    insight,
                    date = currentDate.plusMonths(i),
                    insight.difference_float
                )
            )
        }
        return insightChartDataList
    }

}