package com.drinkwater.reminder.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drinkwater.reminder.core.data.database.dao.DailySummaryDao
import com.drinkwater.reminder.core.data.database.dao.WaterIntakeDao
import com.drinkwater.reminder.core.data.database.entity.DailySummaryEntity
import com.drinkwater.reminder.core.data.database.entity.WaterIntakeEntity

/**
 * Room Database for Water Reminder App
 * Stores water intake records and daily summaries
 */
@Database(
    entities = [
        WaterIntakeEntity::class,
        DailySummaryEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class WaterReminderDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun dailySummaryDao(): DailySummaryDao
}
