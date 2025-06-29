/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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