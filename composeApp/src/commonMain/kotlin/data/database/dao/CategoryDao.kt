package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import data.database.model.transaction.Category

@Dao
abstract class CategoryDao : BaseDao<Category> {

    @Query("SELECT * FROM categoryTable")
    abstract suspend fun getAllCategories(): List<Category>

    @Query("DELETE FROM categoryTable WHERE category =:category")
    abstract suspend fun deleteCategory(category: String)
}