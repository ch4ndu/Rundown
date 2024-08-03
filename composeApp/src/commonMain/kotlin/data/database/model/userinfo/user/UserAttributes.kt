package data.database.model.userinfo.user

import kotlinx.serialization.Serializable

@Serializable
data class UserAttributes(
        val updated_at: String?,
        val created_at: String?,
        val email: String?,
        val blocked: Boolean,
        val blocked_code: String?,
        val role: String?
)