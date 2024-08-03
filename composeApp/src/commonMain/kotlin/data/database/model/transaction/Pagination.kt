package data.database.model.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
        val total: Int,
        val count: Int,
        val per_page: Int,
        val current_page: Int,
        val total_pages: Int
)