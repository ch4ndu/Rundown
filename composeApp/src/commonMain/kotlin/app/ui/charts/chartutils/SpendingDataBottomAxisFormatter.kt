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

package app.ui.charts.chartutils

import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat


class SpendingDataBottomAxisFormatter(
    private val entries: List<Any>,
    private val dateTimeFormatter: DateTimeFormat<LocalDateTime>
) : CartesianValueFormatter {

    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?
    ): CharSequence {
        if (entries.isNotEmpty()) {
            val entry = entries.getOrNull(value.toInt())
            if (entry is ExpenseIncomeData) {
                return entry.date.format(dateTimeFormatter)
            } else if (entry is ExpenseData) {
                return entry.date.format(dateTimeFormatter)
            }
            return "unknown"
        }
        return "unknown"
    }
}