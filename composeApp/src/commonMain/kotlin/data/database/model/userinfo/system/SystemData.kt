package data.database.model.userinfo.system

import kotlinx.serialization.Serializable

@Serializable
data class SystemData(
        val version: String = "",
        val api_version: String = "",
        val php_version: String = "",
        val os: String = "",
        val driver: String = ""
)