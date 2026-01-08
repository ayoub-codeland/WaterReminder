package com.drinkwater.reminder.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Android implementation of DatabaseFactory
 */
class AndroidDatabaseFactory(
    private val context: Context
) : DatabaseFactory {
    override fun createDatabase(): RoomDatabase.Builder<WaterReminderDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath("water_reminder.db")
        return Room.databaseBuilder<WaterReminderDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
