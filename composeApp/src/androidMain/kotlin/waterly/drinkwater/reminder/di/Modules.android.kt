package waterly.drinkwater.reminder.di

import waterly.drinkwater.reminder.core.data.database.AndroidDatabaseFactory
import waterly.drinkwater.reminder.core.data.database.DatabaseFactory
import waterly.drinkwater.reminder.core.data.datastore.DataStoreFactory
import waterly.drinkwater.reminder.core.data.notification.NotificationScheduler
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory(androidApplication()) }
        single { AndroidDatabaseFactory(androidApplication()) }.bind<DatabaseFactory>()
        single { NotificationScheduler(androidApplication()) }
    }
