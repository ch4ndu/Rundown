package data.database.model.transaction

import kotlinx.serialization.Serializable

@Serializable
data class FireFlyTransactionAttributes(
    val created_at: String,
    val group_title: String? = null,
    val transactions: List<FireFlyTransaction>,
    val updated_at: String,
    val user: Int
)