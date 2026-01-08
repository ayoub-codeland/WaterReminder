package com.drinkwater.reminder.core.data.datastore

import android.content.Context

actual class DataStoreFactory(
    private val context: Context
) {
    actual fun createAppDataStore(): AppDataStore {
        return AppDataStore {
            context.filesDir.resolve(AppDataStore.DATA_STORE_FILE_NAME).absolutePath
        }
    }
}