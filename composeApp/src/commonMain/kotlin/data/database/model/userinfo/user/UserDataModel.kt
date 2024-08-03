package data.database.model.userinfo.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataModel(
    @SerialName("data")
    val userData: UserData
)