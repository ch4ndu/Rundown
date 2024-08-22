package app.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import data.database.serializers.DateSerializer
import domain.model.DateRange
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

object Savers {
    val DateRange: Saver<MutableState<DateRange>, List<String>> = Saver(
        save = {
            listOf(
                it.value.startDate.format(DateSerializer.isoFormat),
                it.value.endDate.format(DateSerializer.isoFormat)
            )
        },
        restore = {
            mutableStateOf(
                DateRange(
                    startDate = LocalDateTime.parse(it[0], DateSerializer.isoFormat),
                    endDate = LocalDateTime.parse(it[1], DateSerializer.isoFormat)
                )
            )
        }
    )
}