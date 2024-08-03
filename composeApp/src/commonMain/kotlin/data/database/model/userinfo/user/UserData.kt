package data.database.model.userinfo.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val type: String,
    val id: String,
    @SerialName("attributes")
    val userAttributes: UserAttributes
)