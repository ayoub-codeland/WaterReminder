package waterly.drinkwater.reminder.core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import waterly.drinkwater.reminder.core.data.database.dao.DailySummaryDao
import waterly.drinkwater.reminder.core.data.database.dao.WaterIntakeDao
import waterly.drinkwater.reminder.core.data.database.entity.DailySummaryEntity
import waterly.drinkwater.reminder.core.data.database.entity.WaterIntakeEntity

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
@ConstructedBy(WaterReminderDatabaseConstructor::class)
abstract class WaterReminderDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun dailySummaryDao(): DailySummaryDao
}

// The Room compiler generates the actual implementation
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object WaterReminderDatabaseConstructor : RoomDatabaseConstructor<WaterReminderDatabase> {
    override fun initialize(): WaterReminderDatabase
}
