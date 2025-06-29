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

package domain.model

import data.database.serializers.DateSerializer
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.minusYearsAtBeginning
import domain.withEndOfMonthAtEndOfDay
import domain.withStartOfDay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


data class DateRange(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
) {
    fun getFilterDisplay(): String {
        return "${
            startDate.format(
                DateSerializer.chartMonthYearFormat
            )
        } - ${
            endDate.format(
                DateSerializer.chartMonthYearFormat
            )
        }"
    }

    fun getMonthsBetween(): Int {
        return startDate.toInstant(TimeZone.currentSystemDefault())
            .monthsUntil(
                endDate.toInstant(TimeZone.currentSystemDefault()),
                TimeZone.currentSystemDefault()
            )
    }

    fun getDebugDisplay(): String {
        return "${
            startDate.format(
                DateSerializer.displayFormat
            )
        } - ${
            endDate.format(
                DateSerializer.displayFormat
            )
        }"
    }

    companion object {
        fun getDefaultRange(): DateRange {

            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).withStartOfDay()
            val lastYearToDate = DateRange(today.minusYearsAtBeginning(1), today)
            return lastYearToDate
        }
    }
}