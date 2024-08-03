package data.network.firefly

import Constants.ACCOUNTS_API_ENDPOINT
import data.database.model.accounts.AccountsModel
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface AccountsService {

    @GET(ACCOUNTS_API_ENDPOINT)
    suspend fun getPaginatedAccountType(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<AccountsModel>

    @GET(ACCOUNTS_API_ENDPOINT)
    suspend fun getNewPaginatedAccountType(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<AccountsModel>

}