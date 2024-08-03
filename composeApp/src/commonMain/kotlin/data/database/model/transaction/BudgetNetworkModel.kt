package data.database.model.transaction

import kotlinx.serialization.Serializable

@Serializable
data class BudgetNetworkModel(
    val data: List<BudgetNetworkData>,
    val meta: Meta
)
