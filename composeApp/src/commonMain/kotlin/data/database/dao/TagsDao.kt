package data.database.dao

import androidx.room.Dao
import androidx.room.Query
import data.database.model.transaction.Tag

@Dao
abstract class TagsDao : BaseDao<Tag> {

    @Query("SELECT * FROM tagsTable")
    abstract suspend fun getAllTags(): List<Tag>

    @Query("DELETE FROM tagsTable WHERE tag =:tag")
    abstract suspend fun deleteTag(tag: String)

    @Query("DELETE FROM tagsTable WHERE tag IN (:tagList)")
    abstract suspend fun deleteTags(tagList: List<String>)
}