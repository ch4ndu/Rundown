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

package data.network.firefly

import Constants.ACCOUNTS_API_ENDPOINT
import Constants.TRANSACTION_API_ENDPOINT
import data.database.model.transaction.FireFlyTransactionModel
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface TransactionService {

    // type - Available values : all, withdrawal, withdrawals, expense, deposit, deposits,
    // income, transfer, transfers, opening_balance, reconciliation, special, specials, default
    @GET("${ACCOUNTS_API_ENDPOINT}/{id}/transactions")
    suspend fun getNewTransactionsByAccountId(
        @Path("id") id: Long,
        @Query("page") page: Int,
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("type") type: String = "all"
    ): Response<FireFlyTransactionModel>

    @GET(TRANSACTION_API_ENDPOINT)
    suspend fun getAllTransactions(
        @Query("page") page: Int,
        @Query("start") startDate: String,
        @Query("end") endDate: String,
        @Query("type") type: String = "all"
    ): Response<FireFlyTransactionModel>
}
