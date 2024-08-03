package data.database.model.charts

import androidx.room.ColumnInfo
import androidx.room.Entity
import data.database.serializers.DateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(primaryKeys = ["date"])
data class ChartData(
    @Serializable(with = DateSerializer::class)
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val date: LocalDateTime,
    val value: Double
)