package data.database.model.transaction

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
data class BudgetAttributes(
    val name: String,
    val auto_budget_amount: Double?,
    val auto_budget_period: String?,
    val auto_budget_type: String?
)