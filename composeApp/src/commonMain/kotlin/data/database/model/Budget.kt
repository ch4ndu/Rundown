package data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.enums.BudgetType
import data.database.model.Budget.Companion.TABLE_NAME
import data.database.model.transaction.BudgetNetworkData

@Entity(tableName = TABLE_NAME)
data class Budget(
    @PrimaryKey
    val id: Long,
    val name: String,
    val type: BudgetType,
    val amount: Long?,
    val period: String?,
) {
    companion object {

        const val TABLE_NAME = "budgetTable"
        fun fromBudgetData(budgetNetworkData: BudgetNetworkData): Budget {
            return Budget(
                id = budgetNetworkData.budgetId,
                name = budgetNetworkData.budgetAttributes.name,
                amount = budgetNetworkData.budgetAttributes.auto_budget_amount?.toLong(),
                period = budgetNetworkData.budgetAttributes.auto_budget_period,
                type = BudgetType.fromApi(budgetNetworkData.budgetAttributes.auto_budget_type)
            )
        }
    }
}
