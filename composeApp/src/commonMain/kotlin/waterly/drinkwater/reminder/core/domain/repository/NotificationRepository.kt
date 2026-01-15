package waterly.drinkwater.reminder.core.domain.repository

import waterly.drinkwater.reminder.core.domain.model.NotificationPreference
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing water reminder notification preferences.
 *
 * Responsibilities:
 * - Persist and retrieve notification preferences
 * - Schedule/cancel local notifications based on preferences
 * - Handle smart scheduling (respect wake/sleep times, daily goal)
 */
interface NotificationRepository {
    /**
     * Get current notification preferences.
     *
     * @return Current preferences or null if not set
     */
    suspend fun getPreferences(): NotificationPreference?

    /**
     * Observe notification preferences changes.
     *
     * @return Flow of preferences
     */
    fun observePreferences(): Flow<NotificationPreference?>

    /**
     * Save notification preferences and reschedule notifications.
     *
     * @param preference New preferences to save
     */
    suspend fun savePreferences(preference: NotificationPreference)

    /**
     * Check if notifications should be sent now based on current time,
     * wake/sleep schedule, and daily goal progress.
     *
     * @return true if notifications should be active now
     */
    suspend fun shouldSendNotification(): Boolean

    /**
     * Cancel all scheduled notifications.
     */
    suspend fun cancelAllNotifications()
}
