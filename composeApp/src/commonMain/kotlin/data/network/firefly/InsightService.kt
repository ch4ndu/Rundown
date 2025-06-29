/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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