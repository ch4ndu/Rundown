package domain.model

import data.database.serializers.DateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toInstant


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
}