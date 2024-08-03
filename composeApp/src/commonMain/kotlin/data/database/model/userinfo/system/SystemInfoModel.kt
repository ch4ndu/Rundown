package data.database.model.userinfo.system

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SystemInfoModel(
    @SerialName("data")
    val systemData: SystemData = SystemData()
)