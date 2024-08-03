package data.database.model.transaction

import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetNetworkData(
    @SerialName("id")
    val budgetId: Long,
    @Embedded
    @SerialName("attributes")
    val budgetAttributes: BudgetAttributes
)