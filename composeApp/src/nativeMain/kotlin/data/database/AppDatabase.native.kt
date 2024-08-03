@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory

actual class AppDatabaseBuilder : DbBuilder {
    actual override fun getDbBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = NSHomeDirectory() + "/fireflyDroid.db"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile,
            factory = {
//                getDbBuilder().build()
                AppDatabase::class.instantiateImpl()
            } // This too will show error
        )
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .setDriver(BundledSQLiteDriver()) // Very important
            .setQueryCoroutineContext(Dispatchers.IO)
    }
}