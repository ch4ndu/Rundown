package data.network.firefly

import Constants
import data.database.model.transaction.BudgetNetworkModel
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface BudgetService {

    @GET(Constants.BUDGET_ENDPOINT)
    suspend fun getAllBudgets(
        @Query("limit") limit: Int = 50,
        @Query("page") page: Int
    ): Response<BudgetNetworkModel>
}