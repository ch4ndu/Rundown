package domain.model

import data.database.model.insight.InsightData
import kotlinx.datetime.LocalDateTime

data class InsightChartData(
    val insightData: InsightData,
    val date: LocalDateTime,
    val value: Double
)