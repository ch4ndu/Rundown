package data.database.model.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
        val pagination: Pagination
)