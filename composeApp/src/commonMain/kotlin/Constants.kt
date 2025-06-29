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

object Constants {

    const val DEMO_REDIRECT_URI = "https://api-docs.firefly-iii.org/oauth2-redirect.html"
    const val OAUTH_API_ENDPOINT = "oauth"
    const val SYSTEM_INFO_ENDPOINT = "api/v1/about"
    const val TRANSACTION_API_ENDPOINT = "api/v1/transactions"
    const val ACCOUNTS_API_ENDPOINT = "api/v1/accounts"
    const val DB_NAME = "firefly.db"
    const val TAGS_API_ENDPOINT = "api/v1/tags"
    const val SEARCH_API_ENDPOINT = "api/v1/search"
    const val PAGE_SIZE = 25
    const val ACCOUNT_CHARTS_API_ENDPOINT = "api/v1/chart/account/overview"
    const val INSIGHT_INCOME_ENDPOINT = "api/v1/insight"
    const val BUDGET_ENDPOINT = "api/v1/budgets"


    const val BILL_API_ENDPONT = "api/v1/bills"
    const val PIGGY_BANK_API_ENDPOINT = "api/v1/piggy_banks"
    const val RECURRENCE_API_ENDPOINT = "api/v1/recurrences"
    const val CURRENCY_API_ENDPOINT = "api/v1/currencies"
    const val SETTINGS_API_ENDPOINT = "api/v1/preferences"
    const val RULES_API_ENDPOINT = "api/v1/rules"
    const val CATEGORY_API_ENDPOINT = "api/v1/categories"
    const val AVAILABLE_BUDGET_API_ENDPOINT = "api/v1/available_budgets"
    const val BUDGET_API_ENDPOINT = "api/v1/budgets"
    const val SUMMARY_API_ENDPOINT = "api/v1/summary/basic"
    const val AUTOCOMPLETE_API_ENDPOINT = "api/v1/autocomplete"
    const val ATTACHMENT_API_ENDPOINT = "/api/v1/attachments"
    const val DATA_API_ENDPOINT = "api/v1/data/destroy"
    const val PIGGY_BANK_CHANNEL = "com.udnahc.fireflydroid.PIGGY_BANK"
    const val PIGGY_BANK_CHANNEL_DESCRIPTION = "Show Piggy Bank Notifications"
    const val BILL_CHANNEL = "com.udnahc.fireflydroid.BILL"
    const val BILL_CHANNEL_DESCRIPTION = "Show Bill Notifications"
    const val TRANSACTION_CHANNEL = "com.udnahc.fireflydroid.TRANSACTION"
    const val TRANSACTION_CHANNEL_DESCRIPTION = "Show Transaction Notifications"
    const val ACCOUNT_CHANNEL = "com.udnahc.fireflydroid.ACCOUNT"
    const val ACCOUNT_CHANNEL_DESCRIPTION = "Show Account Notifications"
    const val GENERAL_NOTIFICATION = "com.udnahc.fireflydroid.GENERAL"

    //        val EXPENSE_TRANSACTION_TYPES = listOf("expense", "withdrawal", "transfer", "transfers")
    val TRANSFER_TRANSACTION_TYPES = listOf("transfer", "transfers")
    val NEW_EXPENSE_TRANSACTION_TYPES = listOf("expense", "withdrawal")
    val NEW_INCOME_TRANSACTION_TYPES = listOf("deposit", "deposits", "income")
//        val INCOME_TRANSACTION_TYPES =
//            listOf("deposit", "deposits", "income", "transfer", "transfers")
}