package data.database.model.accounts

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "userInfo", indices = [Index(value = ["userEmail"], unique = true)])
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = -1L,
    val userEmail: String = "",
    val secretKey: String = "",
    val accessToken: String = "",
    val baseUrl: String = "",
    val clientId: String = "",
    val refreshToken: String = "",
    val tokenExpiry: Long = -1L,
    val authMethod: String = "",
    val activeUser: Boolean = false
)