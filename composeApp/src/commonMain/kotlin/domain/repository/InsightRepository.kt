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