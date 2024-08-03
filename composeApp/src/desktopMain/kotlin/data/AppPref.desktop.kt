package data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class PreferenceStore : DataStoreBuilder {
    actual override fun getDataStore(): DataStore<Preferences> {
        return createDataStoreWithDefaults { SETTINGS_PREFERENCES }
    }
}