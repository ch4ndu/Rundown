package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import data.database.model.Budget
import data.database.model.Budget.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BudgetDao : BaseDao<Budget> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAllBudgets(): Flow<List<Budget>>
}