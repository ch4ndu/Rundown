package data.database.model.transaction

import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FireFlyTransactionData(
    @SerialName("id")
    val transactionId: Long,
    @Embedded
    @SerialName("attributes")
    val transactionAttributes: FireFlyTransactionAttributes
)