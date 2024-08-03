package data.database.model.currency

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class CurrencyAttributes(
        val updated_at: String,
        val created_at: String,
        val enabled: Boolean,
        val name: String,
        val code: String,
        val symbol: String,
        val decimal_places: Int,
        @SerialName("default")
        val currencyDefault: Boolean
)