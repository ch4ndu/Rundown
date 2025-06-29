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

package data.database.model.charts

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.database.serializers.DateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "accountChartData", ignoredColumns = ["entries"])
data class AccountChartData(
    @PrimaryKey(autoGenerate = false)
    val label: String,
    val currency_symbol: String,
    @Serializable(with = DateSerializer::class)
    val start_date: LocalDateTime,
    @Serializable(with = DateSerializer::class)
    val end_date: LocalDateTime,
    val yAxisID: Int,
    val entries: AccountChartEntries = AccountChartEntries(emptyList())
) {
    constructor(
        label: String,
        currency_symbol: String,
        start_date: LocalDateTime,
        end_date: LocalDateTime,
        yAxisID: Int
    ) : this(
        label,
        currency_symbol,
        start_date,
        end_date,
        yAxisID,
        entries = AccountChartEntries(emptyList())
    )
}