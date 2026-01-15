package waterly.drinkwater.reminder.di

import waterly.drinkwater.reminder.core.data.database.DatabaseFactory
import waterly.drinkwater.reminder.core.data.database.IosDatabaseFactory
import waterly.drinkwater.reminder.core.data.datastore.DataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory() }
        single { IosDatabaseFactory() }.bind<DatabaseFactory>()
    }
