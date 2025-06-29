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

import com.patrykandpatrick.vico.core.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import toChartLabel

class MarkerLabelFormatter(
    private val spendingDataList: List<Any>,
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime> = DateSerializer.chartMonthYearFormat,
    private val showSpendingLabel: Boolean,
    colorCode: Boolean = true
) : DefaultCartesianMarker.ValueFormatter {

    override fun format(
        context: CartesianDrawingContext,
        targets: List<CartesianMarker.Target>
    ): CharSequence {
        val index = targets[0].x.toInt()
        val data = spendingDataList[index]
        if (data is ExpenseIncomeData) {
            return data.toChartLabel(dateTimeFormatter)
        } else if (data is ExpenseData) {
            return data.toChartLabel(showSpendingLabel, dateTimeFormatter)
        }
        return "unknown"
    }
}