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

@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package data

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

internal const val SETTINGS_PREFERENCES = "settings_preferences.preferences_pb"

internal fun createDataStoreWithDefaults(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    migrations: List<DataMigration<Preferences>> = emptyList(),
    path: () -> String,
) = PreferenceDataStoreFactory
    .createWithPath(
        corruptionHandler = corruptionHandler,
        scope = coroutineScope,
        migrations = migrations,
        produceFile = {
            path().toPath()
        }
    )

fun createDataStore(
    producePath: () -> String,
): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = null,
    migrations = emptyList(),
    produceFile = { producePath().toPath() },
)

internal const val dataStoreFileName = "meetings.preferences_pb"

interface DataStoreBuilder {
    fun getDataStore(): DataStore<Preferences>
}

expect class PreferenceStore : DataStoreBuilder {
    override fun getDataStore(): DataStore<Preferences>
}

class AppPref(
    private val dataStore: DataStore<Preferences>
) {
    private val firstSyncKey = booleanPreferencesKey("isFirstSyncPending")
    private val lastSyncedAtKey = stringPreferencesKey("lastSyncedAt")
    private val semVersion = stringPreferencesKey("semVersion")
    private val remoteApiVersion = stringPreferencesKey("remoteApiVersion")
    private val userOs = stringPreferencesKey("userOs")
    private val userRole = stringPreferencesKey("userRole")

    suspend fun isFirstSyncPending(): Flow<Boolean> {
        return dataStore.data.map {
            it[firstSyncKey] ?: false
        }
    }

    suspend fun setFirstSyncDone() {
        dataStore.edit {
            it[firstSyncKey] = true
        }
    }

    fun lastSyncedAt(): Flow<String> {
        return dataStore.data.map {
            it[lastSyncedAtKey] ?: ""
        }
    }

    suspend fun setLastSyncedAt(value: String) {
        dataStore.edit {
            it[lastSyncedAtKey] = value
        }
    }

    fun semVersion(): Flow<String> {
        return dataStore.data.map {
            it[semVersion] ?: ""
        }
    }

    suspend fun setSemVersion(value: String) {
        dataStore.edit {
            it[semVersion] = value
        }
    }

    fun remoteApiVersion(): Flow<String> {
        return dataStore.data.map {
            it[remoteApiVersion] ?: ""
        }
    }

    suspend fun setRemoteApiVersion(value: String) {
        dataStore.edit {
            it[remoteApiVersion] = value
        }
    }

    fun userOs(): Flow<String> {
        return dataStore.data.map {
            it[userOs] ?: ""
        }
    }

    suspend fun setUserOs(value: String) {
        dataStore.edit {
            it[userOs] = value
        }
    }

    fun userRole(): Flow<String> {
        return dataStore.data.map {
            it[userRole] ?: ""
        }
    }

    suspend fun setUserRole(value: String) {
        dataStore.edit {
            it[userRole] = value
        }
    }
}