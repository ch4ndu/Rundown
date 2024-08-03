package data.database.model.transaction

import kotlinx.serialization.Serializable

@Serializable
data class FireFlyTransactionModel(
    val data: List<FireFlyTransactionData>,
    val meta: Meta
)