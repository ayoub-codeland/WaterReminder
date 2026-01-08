package com.drinkwater.reminder.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactory(
    private val context: Context
) {
    actual fun createAppDataStore(): DataStore<Preferences> {
        return AppDataStore {
            context.filesDir.resolve(AppDataStore.DATA_STORE_FILE_NAME).absolutePath
        }.db
    }
}