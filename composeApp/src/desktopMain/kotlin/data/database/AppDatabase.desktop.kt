@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class AppDatabaseBuilder : data.database.DbBuilder {
    actual override fun getDbBuilder(): RoomDatabase.Builder<data.database.AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "fireflyDroid.db")
        return Room.databaseBuilder<data.database.AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}