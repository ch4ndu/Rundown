package domain.model

import kotlinx.datetime.LocalDateTime


data class ExpenseIncomeData(
    val expenseAmount: Float,
    val incomeAmount: Float,
    val date: LocalDateTime
)