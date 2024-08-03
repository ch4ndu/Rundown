package data.database.model.charts

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.database.serializers.DateSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "accountChartData", ignoredColumns = ["entries"])
data class AccountChartData(
    @PrimaryKey(autoGenerate = false)
    val label: String,
    val currency_symbol: String,
    @Serializable(with = DateSerializer::class)
    val start_date: LocalDateTime,
    @Serializable(with = DateSerializer::class)
    val end_date: LocalDateTime,
    val yAxisID: Int,
    val entries: AccountChartEntries = AccountChartEntries(emptyList())
) {
    constructor(
        label: String,
        currency_symbol: String,
        start_date: LocalDateTime,
        end_date: LocalDateTime,
        yAxisID: Int
    ) : this(
        label,
        currency_symbol,
        start_date,
        end_date,
        yAxisID,
        entries = AccountChartEntries(emptyList())
    )
}