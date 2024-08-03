package data.database.model.accounts

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class AccountAttributes(
        val account_number: String? = null,
        val name: String,
        val type: String,
        val updated_at: String? = null,
        val created_at: String? = null,
        val active: Boolean = false,
        val account_role: String? = null,
        val currency_id: Long?? = null,
        val currency_code: String? = null,
        val current_balance: Double = 0.0,
        val currency_symbol: String? = null,
        val current_balance_date: String? = null,
        val notes: String? = null,
        val monthly_payment_date: String? = null,
        val credit_card_type: String? = null,
        val iban: String? = null,
        val bic: String? = null,
        val virtual_balance: Double? = null,
        val opening_balance: Double? = null,
        val opening_balance_date: String? = null,
        val liability_type: String? = null,
        val liability_amount: String? = null,
        val liability_start_date: String? = null,
        val interest: String? = null,
        val interest_period: String? = null,
        val include_net_worth: Boolean= false,
        val isPending: Boolean = false
)