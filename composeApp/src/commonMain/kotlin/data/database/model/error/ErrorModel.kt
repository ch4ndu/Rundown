package data.database.model.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(
        val message: String?,
        val errors: Errors?,
        val exception: String?
)