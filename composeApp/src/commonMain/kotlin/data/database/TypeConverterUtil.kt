package data.database

import androidx.room.TypeConverter
import data.enums.BudgetType
import data.database.model.transaction.Tag
import data.network.NetworkClient
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlin.jvm.JvmStatic

@OptIn(ExperimentalSerializationApi::class)
object TypeConverterUtil {

    @TypeConverter
    @JvmStatic
    fun fromBudgetType(budgetType: BudgetType): String? {
        return budgetType.name.lowercase()
    }

    @TypeConverter
    @JvmStatic
    fun toBudgetType(type: String?): BudgetType {
        return BudgetType.fromApi(type)
    }

    @TypeConverter
    @JvmStatic
    fun toTagList(value: String): List<Tag> {
        val temp = value.split(", ")
        if (temp.isEmpty()) {
            return emptyList()
        }
        return temp.map {
            Tag(it)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromTagList(tagList: List<Tag>): String {
        return tagList.joinToString(", ") { it.tag }
    }

    @TypeConverter
    @JvmStatic
    fun toTag(value: String): Tag {
        return Tag(value)
    }

    @TypeConverter
    @JvmStatic
    fun fromTag(tag: Tag): String {
        return tag.tag
    }

    @TypeConverter
    @JvmStatic
    fun toBoolean(value: Boolean): String {
        return value.toString()
    }

    @TypeConverter
    @JvmStatic
    fun fromBoolean(value: String): Boolean {
        return value.toBoolean()
    }

    @TypeConverter
    @JvmStatic
    fun fromStringList(value: List<String>?): String {
        return NetworkClient.json.encodeToString(value)
    }

    @TypeConverter
    @JvmStatic
    fun toList(value: String): List<String> {
        return NetworkClient.json.decodeFromString(value) ?: emptyList()
    }

    @TypeConverter
    @JvmStatic
    fun fromDateTime(value: LocalDateTime?): Long? {
        return value?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
    }

    @TypeConverter
    @JvmStatic
    fun toDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

//    @TypeConverter
//    @JvmStatic
//    fun fromUri(value: String?): Uri? {
//        return value?.toUri()
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun toUri(value: Uri?): String? {
//        return value?.toString()
//    }

    @TypeConverter
    @JvmStatic
    fun fromBudgetType(value: String?): BudgetType? {
        if (value == null) {
            return null
        }
        return when (value) {
            BudgetType.RESET.name -> BudgetType.RESET
            BudgetType.ROLLOVER.name -> BudgetType.ROLLOVER
            BudgetType.NONE.name -> BudgetType.NONE
            else -> null
        }
    }

    @TypeConverter
    @JvmStatic
    fun toBudgetType(value: BudgetType?): String? {
        return value?.name
    }
}
