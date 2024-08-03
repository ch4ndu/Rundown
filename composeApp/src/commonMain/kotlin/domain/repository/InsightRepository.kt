package domain.repository

import data.database.model.insight.InsightData
import data.database.serializers.DateSerializer
import data.network.firefly.InsightService
import di.DispatcherProvider
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

class InsightRepository(
    private val insightService: InsightService,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun getIncomeInsightForAccount(
        accountId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): InsightData? {
        try {
            val networkCall =
                insightService.getTotalIncomeInsightGroupByAssetAccounts(
                    startDate.format(DateSerializer.sendToApiFormat),
                    endDate.format(DateSerializer.sendToApiFormat),
                    null
                )
            val responseBody = networkCall.body()
            if (responseBody != null && networkCall.isSuccessful) {
                val accountInsight = responseBody.find {
                    it.id == accountId.toInt()
                }
                return accountInsight
            }
        } catch (exception: Exception) {
        }
        return null
    }

    suspend fun getTransferInsightForAccount(
        accountId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): InsightData? {
        try {
            val networkCall =
                insightService.getTransferInsightByAssetAccount(
                    startDate.format(DateSerializer.sendToApiFormat),
                    endDate.format(DateSerializer.sendToApiFormat),
                    null
                )
            val responseBody = networkCall.body()
            if (responseBody != null && networkCall.isSuccessful) {
                val transferInsight = responseBody.find {
                    it.id == accountId.toInt()
                }
                return transferInsight
            }
        } catch (exception: Exception) {
        }
        return null
    }

    suspend fun getExpenseInsightForAccount(
        accountId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): InsightData? {
        try {
            val networkCall =
                insightService.getExpenseInsightGroupedByAssetAccount(
                    startDate.format(DateSerializer.sendToApiFormat),
                    endDate.format(DateSerializer.sendToApiFormat),
                    null
                )
            val responseBody = networkCall.body()
            if (responseBody != null && networkCall.isSuccessful) {
                val accountInsight = responseBody.find {
                    it.id == accountId.toInt()
                }
                return accountInsight
            }
        } catch (exception: Exception) {
        }
        return null
    }
}