package com.drinkwater.reminder.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

class AppDataStore(
    private val produceFilePath: () -> String
) {
    val db = PreferenceDataStoreFactory.createWithPath(produceFile = { produceFilePath().toPath() })

    companion object {
        const val DATA_STORE_FILE_NAME = "water_reminder.preferences_pb"
    }
}

expect class DataStoreFactory {
    fun createAppDataStore(): DataStore<Preferences>
}
