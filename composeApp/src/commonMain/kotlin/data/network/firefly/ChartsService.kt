package data.network.firefly

import Constants.ACCOUNT_CHARTS_API_ENDPOINT
import data.database.model.charts.AccountChartData
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface ChartsService {

    @GET(ACCOUNT_CHARTS_API_ENDPOINT)
    suspend fun getChartInfo(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
    ): Response<List<AccountChartData>>
}