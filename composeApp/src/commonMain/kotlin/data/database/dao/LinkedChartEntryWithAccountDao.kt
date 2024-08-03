package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import data.database.model.LinkedChartEntryWithAccount
import data.database.model.charts.ChartData
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
abstract class LinkedChartEntryWithAccountDao : BaseDao<LinkedChartEntryWithAccount> {

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM linkedChartEntryWithAccount WHERE label =:label AND " +
                "(date BETWEEN :startDate AND :endDate) " +
                "ORDER BY date ASC"
    )
    abstract fun getChartEntriesForAccountFlow(
        label: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<ChartData>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM linkedChartEntryWithAccount WHERE label =:label AND " +
                "(date BETWEEN :startDate AND :endDate) " +
                "ORDER BY date ASC"
    )
    abstract suspend fun getChartEntriesForAccount(
        label: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<ChartData>

    @RewriteQueriesToDropUnusedColumns
    @Query("DELETE FROM linkedChartEntryWithAccount WHERE label =:label")
    abstract suspend fun deleteChartEntriesForAccount(label: String)
}