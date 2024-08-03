package data.database.dao

import androidx.paging.PagingData
import androidx.room.Dao
import androidx.room.Query
import data.database.model.charts.AccountChartData
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccountChartDataDao : BaseDao<AccountChartData> {

    @Query("SELECT * FROM accountChartData WHERE label =:label")
    abstract fun getChartDateForAccountFlow(label: String): Flow<AccountChartData?>

    @Query("SELECT * FROM accountChartData WHERE label =:label")
    abstract suspend fun getChartDateForAccount(label: String): AccountChartData?

    @Query("SELECT * FROM accountChartData")
    abstract suspend fun getAllChartData(): List<AccountChartData>
}