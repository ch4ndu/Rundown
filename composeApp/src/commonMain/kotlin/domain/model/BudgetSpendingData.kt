package domain.model

data class BudgetSpendingData(
    val expenseSpent: Double,
    val incomeSpent: Double,
    val totalAmount: Double?,
    val dateRange: DateRange
) {
    val percent by lazy {
        totalAmount?.let {
            expenseSpent.div(it).toFloat()
        } ?: 0f
    }
}