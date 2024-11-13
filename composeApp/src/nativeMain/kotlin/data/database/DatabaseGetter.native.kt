@file:OptIn(ExperimentalForeignApi::class)

package data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.lighthousegames.logging.logging
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

private val log = logging()

fun getRoomDatabase(): AppDatabase {
    val dbPath = documentDirectory() + "/room_database.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbPath,
    )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )

    val applicationSupportDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSApplicationSupportDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    log.d { "currentDirectoryPath: ${NSFileManager.defaultManager.currentDirectoryPath}" }
    log.d { "chandu: documentDirectory: ${documentDirectory?.path}" }
    log.d { "applicationSupportDirectory: ${applicationSupportDirectory?.path}" }
    return requireNotNull(documentDirectory).path!!
//    return requireNotNull(applicationSupportDirectory).path!!
}

fun getApplicationSupportDirectory(): String {
    // Construct the path to the Application Support directory
    val appSupportPath = "${NSHomeDirectory()}/Library/Application Support"
    val fileManager = NSFileManager.defaultManager

    // Check if the directory exists, and create it if it doesn't
    if (!fileManager.fileExistsAtPath(appSupportPath)) {
        fileManager.createDirectoryAtPath(
            appSupportPath,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )
    }

    return appSupportPath
}

fun getDocumentsDirectory(): String {
    val documentsPath = "${NSHomeDirectory()}/Documents"
    val fileManager = NSFileManager.defaultManager

    // Check if the directory exists, and create it if it doesn't
    if (!fileManager.fileExistsAtPath(documentsPath)) {
        fileManager.createDirectoryAtPath(
            documentsPath,
            withIntermediateDirectories = true,
            attributes = null,
            error = null
        )
    }

    return documentsPath
}

fun getLibraryPath(): String {
    return "${NSHomeDirectory()}/Library"
}