package domain.model

import kotlinx.datetime.LocalDateTime

data class Transaction(
    val transactionId: Long,
    val date: LocalDateTime,
    val tags: List<String>,
    val category: String,
    val budgetName: String?,
    val sourceId: Long?,
    val destinationId: Long,
    val destinationName: String,
    val transactionType: String
)