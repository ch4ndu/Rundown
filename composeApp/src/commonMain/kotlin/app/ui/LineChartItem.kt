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

package app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.ui.charts.VicoLineChart
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat

@Composable
expect fun LineChartNative(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartDayMonthYearFormat
)

@Composable
fun LineChart(
    modifier: Modifier,
    chartDataList: List<ExpenseData>?,
    dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartDayMonthYearFormat
) {
    if (chartDataList != null) {
        VicoLineChart(
            modifier = modifier,
            chartDataList = chartDataList,
            dateTimeFormatter = dateTimeFormatter
        )
    }
}