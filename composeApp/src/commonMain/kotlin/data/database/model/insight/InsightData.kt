package data.database.model.insight

import kotlinx.serialization.Serializable

@Serializable
data class InsightData(
    val id: Int?,
    val name: String?,
    val difference_float: Double,
    val currency_id: Int
)