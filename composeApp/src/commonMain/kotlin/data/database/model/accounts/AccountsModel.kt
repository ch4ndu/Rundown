package data.database.model.accounts

import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class AccountsModel(
    @Embedded
        val data: List<AccountData>,
    val meta: Meta
)