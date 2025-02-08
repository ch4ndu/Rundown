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