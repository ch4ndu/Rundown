package data.database.model.currency

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "currency")
data class CurrencyData(
        @PrimaryKey(autoGenerate = false)
        @SerialName("id")
        val currencyId: Long,
        @Embedded
        @SerialName("attributes")
        val currencyAttributes: CurrencyAttributes
)