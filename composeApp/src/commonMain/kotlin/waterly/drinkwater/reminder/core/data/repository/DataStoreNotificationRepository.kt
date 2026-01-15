package waterly.drinkwater.reminder.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import waterly.drinkwater.reminder.core.data.notification.NotificationScheduler
import waterly.drinkwater.reminder.core.domain.model.NotificationPreference
import waterly.drinkwater.reminder.core.domain.repository.NotificationRepository
import waterly.drinkwater.reminder.core.domain.repository.UserProfileRepository
import waterly.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStore-based implementation of NotificationRepository.
 *
 * Responsibilities:
 * - Persist notification preferences using DataStore
 * - Coordinate with platform-specific NotificationScheduler
 * - Implement smart scheduling logic (wake/sleep times, daily goal)
 */
class DataStoreNotificationRepository(
    private val dataStore: DataStore<Preferences>,
    private val notificationScheduler: NotificationScheduler,
    private val waterIntakeRepository: WaterIntakeRepository,
    private val userProfileRepository: UserProfileRepository
) : NotificationRepository {

    private object Keys {
        val IS_ENABLED = booleanPreferencesKey("notification_enabled")
        val FREQUENCY_MINUTES = intPreferencesKey("notification_frequency_minutes")
        val WAKE_UP_TIME = stringPreferencesKey("notification_wake_up_time")
        val BEDTIME = stringPreferencesKey("notification_bedtime")
        val PAUSE_WHEN_GOAL_REACHED = booleanPreferencesKey("notification_pause_when_goal_reached")
    }

    override suspend fun getPreferences(): NotificationPreference? {
        return observePreferences().first()
    }

    override fun observePreferences(): Flow<NotificationPreference?> {
        return dataStore.data.map { preferences ->
            // If no preferences saved yet, return default
            if (!preferences.contains(Keys.IS_ENABLED)) {
                return@map NotificationPreference()
            }

            NotificationPreference(
                isEnabled = preferences[Keys.IS_ENABLED] ?: true,
                frequencyMinutes = preferences[Keys.FREQUENCY_MINUTES] ?: 60,
                wakeUpTime = preferences[Keys.WAKE_UP_TIME] ?: "07:00",
                bedtime = preferences[Keys.BEDTIME] ?: "22:30",
                pauseWhenGoalReached = preferences[Keys.PAUSE_WHEN_GOAL_REACHED] ?: true
            )
        }
    }

    override suspend fun savePreferences(preference: NotificationPreference) {
        // Save to DataStore
        dataStore.edit { preferences ->
            preferences[Keys.IS_ENABLED] = preference.isEnabled
            preferences[Keys.FREQUENCY_MINUTES] = preference.frequencyMinutes
            preferences[Keys.WAKE_UP_TIME] = preference.wakeUpTime
            preferences[Keys.BEDTIME] = preference.bedtime
            preferences[Keys.PAUSE_WHEN_GOAL_REACHED] = preference.pauseWhenGoalReached
        }

        // Reschedule notifications
        if (preference.isEnabled) {
            notificationScheduler.scheduleNotifications(preference)
        } else {
            notificationScheduler.cancelAllNotifications()
        }
    }

    override suspend fun shouldSendNotification(): Boolean {
        val preference = getPreferences() ?: return false

        // Check if notifications are enabled
        if (!preference.isEnabled) {
            return false
        }

        // Check if we're within wake/sleep hours
        if (!isWithinActiveHours(preference)) {
            return false
        }

        // Check if goal is reached and we should pause
        if (preference.pauseWhenGoalReached) {
            val todayTotal = waterIntakeRepository.getTodayTotalFlow().first()
            val userProfile = userProfileRepository.getProfile()
            val dailyGoal = userProfile?.dailyGoal ?: 2500
            if (todayTotal >= dailyGoal) {
                return false
            }
        }

        return true
    }

    override suspend fun cancelAllNotifications() {
        notificationScheduler.cancelAllNotifications()
    }

    /**
     * Check if current time is within active notification hours.
     *
     * @param preference Notification preferences with wake/sleep times
     * @return true if within active hours
     */
    private fun isWithinActiveHours(preference: NotificationPreference): Boolean {
        try {
            val currentTime = getCurrentTime()
            val wakeTime = parseTime(preference.wakeUpTime)
            val sleepTime = parseTime(preference.bedtime)

            // Handle case where bedtime is after midnight (e.g., 02:00)
            return if (sleepTime < wakeTime) {
                // Bedtime is next day (e.g., wake at 07:00, sleep at 02:00)
                currentTime >= wakeTime || currentTime < sleepTime
            } else {
                // Normal case (e.g., wake at 07:00, sleep at 22:30)
                currentTime in wakeTime..sleepTime
            }
        } catch (e: Exception) {
            // If parsing fails, allow notifications
            return true
        }
    }

    /**
     * Get current time in minutes since midnight.
     * @return Minutes since midnight (0-1439)
     */
    private fun getCurrentTime(): Int {
        // Get current epoch milliseconds
        val currentMillis = System.currentTimeMillis()

        // Convert to hours and minutes
        val totalMinutes = (currentMillis / 60000) % 1440 // Minutes in a day

        // Get timezone offset
        val timezoneOffset = java.util.TimeZone.getDefault().getOffset(currentMillis) / 60000

        // Add timezone offset and wrap around 24 hours
        return ((totalMinutes + timezoneOffset) % 1440).toInt()
    }

    /**
     * Parse time string to minutes since midnight.
     * @param time Time string in format "HH:mm"
     * @return Minutes since midnight
     */
    private fun parseTime(time: String): Int {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        return hour * 60 + minute
    }
}
