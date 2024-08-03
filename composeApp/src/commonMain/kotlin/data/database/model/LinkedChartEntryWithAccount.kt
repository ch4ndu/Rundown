package data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Entity(
    tableName = "linkedChartEntryWithAccount",
    indices = [Index(value = ["date"])]
)
data class LinkedChartEntryWithAccount(
    @PrimaryKey(autoGenerate = false)
    val hash: Int,
    val date: LocalDateTime,
    val label: String,
    val value: Double
) {
    constructor(
        label: String,
        date: LocalDateTime,
        value: Double
    ) :
            this(
                label.hashCode() + date.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds().toInt(),
                date,
                label,
                value
            )

}