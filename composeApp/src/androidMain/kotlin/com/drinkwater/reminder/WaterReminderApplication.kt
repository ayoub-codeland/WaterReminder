package com.drinkwater.reminder

import android.app.Application
import com.drinkwater.reminder.di.initKoin
import org.koin.android.ext.koin.androidContext

class WaterReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@WaterReminderApplication)
        }
    }
}