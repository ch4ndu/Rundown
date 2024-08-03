package data.database.model.charts

import data.database.serializers.AccountChartEntriesSerializer
import kotlinx.serialization.Serializable

@Serializable(with = AccountChartEntriesSerializer::class)
data class AccountChartEntries(
    val entries: List<ChartData>
)