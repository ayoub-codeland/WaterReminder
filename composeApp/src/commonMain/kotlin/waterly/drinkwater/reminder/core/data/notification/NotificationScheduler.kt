package waterly.drinkwater.reminder.core.data.notification

import waterly.drinkwater.reminder.core.domain.model.NotificationPreference

/**
 * Platform-specific notification scheduler interface.
 *
 * Uses expect/actual pattern to provide:
 * - Android: WorkManager for periodic notifications
 * - iOS: UNUserNotificationCenter (to be implemented when needed)
 */
expect class NotificationScheduler {
    /**
     * Schedule periodic water reminder notifications based on preferences.
     *
     * @param preference Notification preferences (frequency, times, etc.)
     */
    suspend fun scheduleNotifications(preference: NotificationPreference)

    /**
     * Cancel all scheduled water reminder notifications.
     */
    suspend fun cancelAllNotifications()

    /**
     * Check if notification permission is granted.
     *
     * @return true if permission granted, false otherwise
     */
    suspend fun hasNotificationPermission(): Boolean
}
