package com.drinkwater.reminder

import android.app.Application
import com.drinkwater.reminder.core.data.local.initializeDataStore
import com.drinkwater.reminder.di.appModules
import com.drinkwater.reminder.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WaterReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize DataStore (existing)
        initializeDataStore(this)

        // Initialize Koin
        startKoin {
            androidContext(this@WaterReminderApplication)
            modules(appModules() + platformModule())
        }
    }
}