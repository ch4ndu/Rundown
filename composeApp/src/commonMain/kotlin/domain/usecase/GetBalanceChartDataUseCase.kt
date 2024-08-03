package domain.usecase

import data.database.model.accounts.AccountData
import di.DispatcherProvider
import domain.model.DateRange
import domain.model.GroupedExpenseData
import domain.repository.ChartsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBalanceChartDataUseCase(
    private val chartsRepository: ChartsRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    fun accountChartData(
        dateRange: DateRange,
        accountName: String,
    ): Flow<GroupedExpenseData?> {
        return chartsRepository.loadLocalChartDataForAccountFlow(
            dateRange.startDate,
            dateRange.endDate,
            accountName
        ).flowOn(dispatcherProvider.io)
    }

    suspend fun getCombinedChartData(
        dateRange: DateRange,
        accountName: List<AccountData>
    ): Flow<GroupedExpenseData?> {
        return chartsRepository.loadLocalCombinedChartDataFlow(
            startDate = dateRange.startDate,
            endDate = dateRange.endDate,
            accountNameList = accountName
        )
    }
}