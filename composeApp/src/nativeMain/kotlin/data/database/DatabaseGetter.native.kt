/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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
    val dbPath = documentDirectory() + "/rundown.db"
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
    return requireNotNull(documentDirectory).path!!
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