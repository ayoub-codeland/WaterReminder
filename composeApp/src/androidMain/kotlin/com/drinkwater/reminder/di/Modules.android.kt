package com.drinkwater.reminder.di

import com.drinkwater.reminder.core.data.database.AndroidDatabaseFactory
import com.drinkwater.reminder.core.data.database.DatabaseFactory
import com.drinkwater.reminder.core.data.datastore.DataStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory(androidApplication()) }
        single { AndroidDatabaseFactory(androidApplication()) }.bind<DatabaseFactory>()
    }
