package data.database.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthModel(
        val token_type: String,
        val expires_in: Long,
        val access_token: String,
        val refresh_token: String
)