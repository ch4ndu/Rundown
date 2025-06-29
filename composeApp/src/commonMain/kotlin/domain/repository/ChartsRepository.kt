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

import data.database.dao.AccountChartDataDao
import data.database.dao.LinkedChartEntryWithAccountDao
import data.database.model.LinkedChartEntryWithAccount
import data.database.model.accounts.AccountData
import data.database.model.charts.AccountChartData
import data.database.model.charts.AccountChartEntries
import data.database.model.charts.ChartData
import data.database.serializers.DateSerializer
import data.network.firefly.ChartsService
import di.DispatcherProvider
import domain.model.DateRange
import domain.model.ExpenseData
import domain.model.GroupedExpenseData
import domain.withStartOfDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import org.lighthousegames.logging.logging

class ChartsRepository(
    private val accountChartDataDao: AccountChartDataDao,
    private val linkedChartEntryWithAccountDao: LinkedChartEntryWithAccountDao,
    private val chartsService: ChartsService,
    private val dispatcherProvider: DispatcherProvider
) {
    private val log = logging()

    suspend fun loadRemoteChartData(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<AccountChartData> {
        return withContext(dispatcherProvider.io) {
            try {
                val networkCall = chartsService.getChartInfo(
                    startDate.format(DateSerializer.sendToApiFormat),
                    endDate.format(DateSerializer.sendToApiFormat)
                )
                val responseBody = networkCall.body()
                if (responseBody != null && networkCall.isSuccessful) {
                    log.d { "loadRemoteChartData response-$responseBody" }
                    responseBody.forEach { accountChartData ->
                        val accountName = accountChartData.label
                        accountChartDataDao.insert(accountChartData)
                        val list = accountChartData.entries.entries.map {
                            LinkedChartEntryWithAccount(
                                label = accountName,
                                date = it.date.withStartOfDay(),
                                value = it.value
                            )
                        }
                        linkedChartEntryWithAccountDao.insertAll(list)
                    }
                    responseBody
                } else {
                    emptyList()
                }

            } catch (exception: Exception) {
                log.e(exception) { "loadRemoteChartDate" }
                emptyList()
            }
        }
    }

//    fun loadLocalChartDataForAccountFlow(
//        startDate: DateTime,
//        endDate: DateTime,
//        accountName: String
//    ): Flow<AccountChartData?> {
//        return combine(
//            accountChartDataDao.getChartDateForAccountFlow(accountName)
//                .flowOn(dispatcherProvider.io),
//            linkedChartEntryWithAccountDao.getChartEntriesForAccountFlow(
//                label = accountName,
//                startDate = startDate,
//                endDate = endDate
//            ).flowOn(dispatcherProvider.io)
//        ) { accountChartData: AccountChartData?, chartEntryData: List<ChartData> ->
//            accountChartData?.copy(entries = AccountChartEntries(chartEntryData))
//        }
//            .distinctUntilChanged()
//    }

    fun loadLocalChartDataForAccountFlow(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountName: String
    ): Flow<GroupedExpenseData?> {
        return combine(
            accountChartDataDao.getChartDateForAccountFlow(accountName)
                .flowOn(dispatcherProvider.io),
            linkedChartEntryWithAccountDao.getChartEntriesForAccountFlow(
                label = accountName,
                startDate = startDate,
                endDate = endDate
            ).flowOn(dispatcherProvider.io)
        ) { accountChartData: AccountChartData?, chartEntryData: List<ChartData> ->
//            accountChartData?.copy(entries = AccountChartEntries(chartEntryData))
            GroupedExpenseData(
                dateRange = DateRange(startDate, endDate),
                name = accountName,
                expenseDataList = chartEntryData.map {
                    ExpenseData(
                        expenseAmount = it.value.toFloat(),
                        date = it.date
                    )
                }.sortedByDescending { it.date }
            )
        }
            .distinctUntilChanged()
    }

    suspend fun loadLocalCombinedChartDataFlow(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        accountNameList: List<AccountData>
    ): Flow<GroupedExpenseData> {
        return withContext(dispatcherProvider.io) {
            val chartEntrySet = HashMap<LocalDateTime, Double>()
            val chartDataList = accountNameList.mapNotNull { accountData ->
                val accountChartData =
                    accountChartDataDao.getChartDateForAccount(accountData.attributes.name)
                val entries = linkedChartEntryWithAccountDao.getChartEntriesForAccount(
                    label = accountData.attributes.name,
                    startDate = startDate,
                    endDate = endDate
                )
                accountChartData?.copy(entries = AccountChartEntries(entries))
            }
            chartDataList.forEach { accountChartData ->
                accountChartData.entries.entries.forEach { chartData ->
                    val amount = chartEntrySet[chartData.date]
                    if (amount == null) {
                        chartEntrySet[chartData.date] = chartData.value
                    } else {
                        chartEntrySet[chartData.date] = amount + chartData.value
                    }
                }
            }
            val entries = chartEntrySet.map {
                ExpenseData(
                    expenseAmount = it.value.toFloat(),
                    date = it.key
                )
            }
                .sortedByDescending { it.date }
            flowOf(
                GroupedExpenseData(
                    name = "Combined",
                    dateRange = DateRange(startDate = startDate, endDate = endDate),
                    expenseDataList = entries
                )
            )
        }
    }
}