package data.database.model.accounts

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "accounts")
data class AccountData(
        @PrimaryKey(autoGenerate = false)
        val id: Long = 0,
        @Embedded
        val attributes: AccountAttributes
)