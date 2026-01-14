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
     * TEST MODE: Set TEST_MODE_ENABLED = true to use seconds instead of minutes.
     * This allows testing with intervals like 30 seconds instead of 30 minutes.
     *
     * @param preference Notification preferences
     */
    actual suspend fun scheduleNotifications(preference: NotificationPreference) {
        // Cancel existing work first
        cancelAllNotifications()

        // TEST MODE: Change to true for quick testing (uses seconds instead of minutes)
        val TEST_MODE_ENABLED = false

        val (interval, timeUnit) = if (TEST_MODE_ENABLED) {
            // Test mode: Use seconds (minimum 15 seconds for WorkManager)
            // Set frequency to 30 in UI = notification every 30 SECONDS
            val seconds = preference.frequencyMinutes.coerceAtLeast(15)
            Pair(seconds.toLong(), TimeUnit.SECONDS)
        } else {
            // Production: Use minutes (minimum 15 minutes for WorkManager)
            val minutes = preference.frequencyMinutes.coerceAtLeast(15)
            Pair(minutes.toLong(), TimeUnit.MINUTES)
        }

        // Create periodic work request
        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            repeatInterval = interval,
            repeatIntervalTimeUnit = timeUnit
        )
            .build()

        // Enqueue work (replace existing with same name)
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
