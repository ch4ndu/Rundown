package data.network.firefly

import Constants
import data.database.model.insight.InsightData
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface InsightService {

    // single object in array
    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/income/total")
    suspend fun getTotalIncomeInsightForAccounts(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<InsightData>

//    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/income/total")
//    suspend fun getTotalIncomeInsight(
//        @Query("start") startDate: String,
//        @Query("end") endDate: String
//    ): Response<InsightData>

    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/income/revenue")
    suspend fun getTotalIncomeInsightGroupByRevenueAccounts(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<InsightData>

    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/income/asset")
    suspend fun getTotalIncomeInsightGroupByAssetAccounts(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<List<InsightData>>

    // single object in array
    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/expense/total")
    suspend fun getTotalExpenseInsightForAccounts(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<List<InsightData>>

    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/expense/expense")
    suspend fun getExpenseInsightGroupedByExpenseAccount(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<List<InsightData>>

    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/expense/asset")
    suspend fun getExpenseInsightGroupedByAssetAccount(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<List<InsightData>>

    @GET("${Constants.INSIGHT_INCOME_ENDPOINT}/transfer/asset")
    suspend fun getTransferInsightByAssetAccount(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("account[]") accountList: List<String>?
    ): Response<List<InsightData>>
}