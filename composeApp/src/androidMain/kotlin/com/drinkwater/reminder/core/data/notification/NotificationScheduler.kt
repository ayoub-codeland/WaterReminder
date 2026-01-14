package com.drinkwater.reminder.core.data.notification

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.drinkwater.reminder.core.domain.model.NotificationPreference
import java.util.concurrent.TimeUnit

/**
 * Android implementation of NotificationScheduler using WorkManager.
 *
 * Schedules periodic work to send water reminder notifications.
 * Respects user preferences for frequency and smart scheduling.
 */
actual class NotificationScheduler(
    private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    /**
     * Schedule periodic water reminder notifications.
     *
     * Uses WorkManager with PeriodicWorkRequest based on user's frequency preference.
     * Minimum frequency is 15 minutes (WorkManager limitation).
     *
     * @param preference Notification preferences
     */
    actual suspend fun scheduleNotifications(preference: NotificationPreference) {
        cancelAllNotifications()

        val frequencyMinutes = preference.frequencyMinutes.coerceAtLeast(15)

        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            repeatInterval = frequencyMinutes.toLong(),
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .build()

        workManager.enqueueUniquePeriodicWork(
            WaterReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    /**
     * Cancel all scheduled water reminder notifications.
     */
    actual suspend fun cancelAllNotifications() {
        workManager.cancelUniqueWork(WaterReminderWorker.WORK_NAME)

        // Also cancel any currently showing notifications
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(WaterReminderWorker.NOTIFICATION_ID)
    }

    /**
     * Check if notification permission is granted.
     *
     * For Android 13+ (API 33+), requires POST_NOTIFICATIONS permission.
     * For older versions, always returns true.
     *
     * @return true if permission granted
     */
    actual suspend fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // No runtime permission needed for older Android versions
            true
        }
    }
}
