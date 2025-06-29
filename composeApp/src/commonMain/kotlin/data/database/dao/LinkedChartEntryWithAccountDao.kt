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

package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import data.database.model.LinkedChartEntryWithAccount
import data.database.model.charts.ChartData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
abstract class LinkedChartEntryWithAccountDao : BaseDao<LinkedChartEntryWithAccount> {

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM linkedChartEntryWithAccount WHERE label =:label AND " +
                "(date BETWEEN :startDate AND :endDate) " +
                "ORDER BY date ASC"
    )
    abstract fun getChartEntriesForAccountFlow(
        label: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<ChartData>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM linkedChartEntryWithAccount WHERE label =:label AND " +
                "(date BETWEEN :startDate AND :endDate) " +
                "ORDER BY date ASC"
    )
    abstract suspend fun getChartEntriesForAccount(
        label: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<ChartData>

    @RewriteQueriesToDropUnusedColumns
    @Query("DELETE FROM linkedChartEntryWithAccount WHERE label =:label")
    abstract suspend fun deleteChartEntriesForAccount(label: String)
}