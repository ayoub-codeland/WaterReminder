package waterly.drinkwater.reminder.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

/**
 * iOS implementation of DatabaseFactory
 */
class IosDatabaseFactory : DatabaseFactory {
    override fun createDatabase(): RoomDatabase.Builder<WaterReminderDatabase> {
        val dbFilePath = NSHomeDirectory() + "/Documents/water_reminder.db"
        return Room.databaseBuilder<WaterReminderDatabase>(
            name = dbFilePath
        )
    }
}
