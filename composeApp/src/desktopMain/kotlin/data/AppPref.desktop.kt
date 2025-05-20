package data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PreferenceStore : DataStoreBuilder {
    actual override fun getDataStore(): DataStore<Preferences> {
        return createDataStoreWithDefaults { SETTINGS_PREFERENCES }
    }
}