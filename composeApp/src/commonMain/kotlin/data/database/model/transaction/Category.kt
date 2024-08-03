package data.database.model.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "categoryTable")
data class Category(
    @PrimaryKey
    val category: String
)