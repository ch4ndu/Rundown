package domain.model

import domain.model.DateRange
import domain.model.ExpenseData

data class GroupedExpenseData(
    val name: String,
    val dateRange: DateRange,
    val expenseDataList: List<ExpenseData>
)
