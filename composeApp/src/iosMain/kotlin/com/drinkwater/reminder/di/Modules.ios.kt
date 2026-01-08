package com.drinkwater.reminder.di

import com.drinkwater.reminder.core.data.database.DatabaseFactory
import com.drinkwater.reminder.core.data.database.IosDatabaseFactory
import com.drinkwater.reminder.core.data.datastore.DataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory() }
        single { IosDatabaseFactory() }.bind<DatabaseFactory>()
    }
