package com.drinkwater.reminder.core.data.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

class AppDataStore(
    private val produceFilePath: () -> String
) {
    val db =
        PreferenceDataStoreFactory.createWithPath(produceFile = { produceFilePath().toPath() })

    companion object {
        const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"
    }
}

expect class DataStoreFactory {
    fun createAppDataStore(): AppDataStore
}
