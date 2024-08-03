@file:Suppress("PropertyName")

package data.database.model.transaction

import Constants
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import data.database.model.transaction.FireFlyTransaction.Companion.TABLE_NAME
import data.database.serializers.DateSerializer
import getDisplayWithCurrency
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(
            value = [
                "category_id",
                "date",
                "source_id",
                "destination_id",
                "transactionType",
                "tags"
            ]
        )
    ]
)
data class FireFlyTransaction(
    @PrimaryKey(autoGenerate = false)
    val transaction_journal_id: Long,
    @Serializable(with = DateSerializer::class)
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER) val date: LocalDateTime,
    @EncodeDefault
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT) val tags: List<Tag> = emptyList(),
    @EncodeDefault
    val category_name: String = "Uncategorized",
    @SerialName("type")
    val transactionType: String,
    val amount: Double,
    val budget_id: Long?,
    val budget_name: String?,
    val category_id: Long?,
    val currency_code: String,
    val currency_decimal_places: Int,
    val currency_id: Long,
    val currency_name: String,
    val currency_symbol: String,
    val description: String,
    val destination_id: Long,
    val destination_name: String,
    val destination_type: String,
    val internal_reference: String?,
    val isPending: Boolean = false,
    val notes: String?,
    val order: Int,
    val source_iban: String?,
    var source_id: Long?,
    val source_name: String?,
    val source_type: String?,
    val user: Int
) {
    val amountTextColor by lazy {
        if (Constants.NEW_EXPENSE_TRANSACTION_TYPES.contains(transactionType)) {
            WalletOrange2
        } else if (Constants.NEW_INCOME_TRANSACTION_TYPES.contains(transactionType)) {
            WalletLightGreen
        } else if (Constants.TRANSFER_TRANSACTION_TYPES.contains(transactionType)) {
            WalletBlue2
        } else {
            Color.Unspecified
        }
    }
    val amountTextColorForSameSource by lazy {
        if (Constants.NEW_EXPENSE_TRANSACTION_TYPES.contains(transactionType) ||
            Constants.TRANSFER_TRANSACTION_TYPES.contains(transactionType)
        ) {
            WalletOrange2
        } else if (Constants.NEW_INCOME_TRANSACTION_TYPES.contains(transactionType)) {
            WalletLightGreen
        } else {
            Color.Unspecified
        }
    }

    val displayWithCurrency by lazy {
        amount.getDisplayWithCurrency(currency_symbol)
    }

    fun getTagList(): List<Tag> {
        return tags
    }

    companion object {

        val WalletOrange2 = Color(0xFFF44336) // negative amounts
        val WalletLightGreen = Color(0xFF21CB87) // positive amount
        val WalletBlue2 = Color(0xFF6099EB)

        const val TABLE_NAME = "transactionTable"
        fun getMockTransaction(): FireFlyTransaction {
            return FireFlyTransaction(
                description = "test description",
                amount = 10.0,
                budget_id = null,
                budget_name = "budgetName",
                category_id = null,
                category_name = "Category",
                currency_code = "$",
                currency_decimal_places = 1,
                currency_id = 1,
                destination_id = 1,
                destination_type = "asset",
                currency_name = "currencyName",
                currency_symbol = "$",
                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                destination_name = "destinationName",
                internal_reference = null,
                isPending = false,
                notes = null,
                order = 1,
                source_iban = null,
                source_id = null,
                source_name = null,
                source_type = null,
                transaction_journal_id = 1,
                transactionType = "expense",
                tags = emptyList(),
                user = 123
            )
        }
    }
}