package domain.model

import data.database.model.Budget

data class BudgetSpending(
    val budget: Budget,
    val expenseIncomeDataList: List<ExpenseData>
)
