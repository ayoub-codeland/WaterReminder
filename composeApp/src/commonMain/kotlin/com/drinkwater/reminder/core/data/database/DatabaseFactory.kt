package com.drinkwater.reminder.core.data.database

import androidx.room.RoomDatabase

/**
 * Factory interface for creating the database
 * Platform-specific implementations will provide the actual builder
 */
interface DatabaseFactory {
    fun createDatabase(): RoomDatabase.Builder<WaterReminderDatabase>
}
