package data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppDatabaseBuilder(private val context: Context) : DbBuilder {
    actual override fun getDbBuilder(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = context.getDatabasePath("fireflyDroid.db")
        return Room.databaseBuilder(
            context = context,
            name = dbFile.absolutePath,
            factory = { AppDatabase::class.instantiateImpl() }
        )
            .setDriver(BundledSQLiteDriver()) // Very important
            .setQueryExecutor(Dispatchers.IO.asExecutor())
            .fallbackToDestructiveMigration(false)
    }

}