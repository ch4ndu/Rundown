package domain.model

data class GroupedExpenseData(
    val name: String,
    val dateRange: DateRange,
    val expenseDataList: List<ExpenseData>
)
