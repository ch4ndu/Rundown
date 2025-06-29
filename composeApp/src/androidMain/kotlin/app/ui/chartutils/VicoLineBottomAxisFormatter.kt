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

package app.ui.chartutils

import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import data.database.serializers.DateSerializer
import domain.model.ExpenseData
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime


class VicoLineBottomAxisFormatter(private val entries: List<ExpenseData?>) :
    CartesianValueFormatter {

    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?
    ): CharSequence {
        if (entries.isNotEmpty()) {
            val date =
                entries.getOrNull(value.toInt())?.date
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return date.format(DateSerializer.chartMonthYearFormat)
        }
        return "unknown"
    }
}