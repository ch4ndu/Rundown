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

package domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


fun currentDate(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

fun LocalDateTime.withStartOfDay(): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.date.atStartOfDayIn(tz).toLocalDateTime(tz)
}

fun LocalDateTime.millis(): Long {
    return this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDateTime.atBeginningOfMonth(): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()

    return this.date.atStartOfDayIn(tz)
        .minus(period = DateTimePeriod(days = this.dayOfMonth - 1), timeZone = tz)
        .toLocalDateTime(tz)
}

fun LocalDateTime.plusMonths(months: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.toInstant(tz)
        .plus(period = DateTimePeriod(months = months), tz)
        .toLocalDateTime(tz)
}

fun LocalDateTime.minusMonths(months: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.toInstant(tz)
        .minus(period = DateTimePeriod(months = months), tz)
        .toLocalDateTime(tz)
}

fun LocalDateTime.plusMonthsAtBeginning(months: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()

    return this.date.atStartOfDayIn(tz)
        .plus(period = DateTimePeriod(months = months), tz)
        .minus(
            period = DateTimePeriod(days = this.dayOfMonth - 1),
            timeZone = tz
        )
        .toLocalDateTime(tz)
}

fun LocalDateTime.minusDaysAtBeginning(days: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.date
        .atStartOfDayIn(tz)
        .minus(period = DateTimePeriod(days = days), tz)
        .toLocalDateTime(tz)
}

fun LocalDateTime.plusDaysAtBeginning(days: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.date
        .atStartOfDayIn(tz)
        .plus(period = DateTimePeriod(days = days), tz)
        .toLocalDateTime(tz)
}

fun LocalDateTime.withEndOfMonthAtEndOfDay(): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.date
        .atStartOfDayIn(tz)
        .plus(
            period = DateTimePeriod(
                days = this.month.number.monthLength(isLeapYear(this.year)) - this.dayOfMonth
            ),
            timeZone = tz
        ).plus(period = DateTimePeriod(hours = 23, minutes = 59, seconds = 59), timeZone = tz)
        .toLocalDateTime(tz)
}

fun LocalDateTime.withStartOfYear(): LocalDateTime {
    return this.minusMonths(monthNumber - 1).atBeginningOfMonth()
}

fun LocalDateTime.minusYearsAtBeginning(years: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.date
        .atStartOfDayIn(tz)
        .minus(period = DateTimePeriod(years = years), timeZone = tz)
        .toLocalDateTime(tz)
        .minusMonths(monthNumber - 1)
        .atBeginningOfMonth()
}

fun LocalDateTime.minusYearsAtEnd(years: Int): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return this.date
        .atStartOfDayIn(tz)
        .minus(period = DateTimePeriod(years = years), timeZone = tz)
        .plus(period = DateTimePeriod(months = 12 - monthNumber), timeZone = tz)
        .plus(
            period = DateTimePeriod(
                days = this.month.number.monthLength(isLeapYear(this.year)) - this.dayOfMonth
            ), timeZone = tz
        ).plus(period = DateTimePeriod(hours = 23, minutes = 59, seconds = 59), timeZone = tz)
        .toLocalDateTime(tz)
}

private fun Int.monthLength(isLeapYear: Boolean): Int {
    return when (this) {
        2 -> if (isLeapYear) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
}

private fun isLeapYear(year: Int): Boolean {
    val prolepticYear: Long = year.toLong()
    return prolepticYear and 3 == 0L && (prolepticYear % 100 != 0L || prolepticYear % 400 == 0L)
}