package domain.model

import kotlinx.datetime.LocalDateTime


data class ExpenseData(
    val expenseAmount: Float,
    val date: LocalDateTime
)