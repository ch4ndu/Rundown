package data.database.model.transaction

import androidx.room.Entity
import androidx.room.PrimaryKey
import data.database.serializers.TagsSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TagsSerializer::class)
@Entity(tableName = "tagsTable")
data class Tag(
    @PrimaryKey
    val tag: String
)