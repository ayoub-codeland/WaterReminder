package com.drinkwater.reminder.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.drinkwater.reminder.core.data.notification.NotificationScheduler
import com.drinkwater.reminder.core.domain.model.NotificationPreference
import com.drinkwater.reminder.core.domain.repository.NotificationRepository
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
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
        // TODO: Implement time range check
        // Parse wakeUpTime and bedtime (format: "HH:mm")
        // Compare with current time
        // Handle edge cases (e.g., bedtime after midnight)

        // For MVP, always return true
        // This will be implemented in the Worker when checking if notification should be shown
        return true
    }
}
