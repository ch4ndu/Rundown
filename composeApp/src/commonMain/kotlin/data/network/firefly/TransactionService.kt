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
