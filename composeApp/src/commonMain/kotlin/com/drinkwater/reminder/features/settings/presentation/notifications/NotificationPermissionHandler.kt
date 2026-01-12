package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.compose.runtime.Composable

/**
 * Platform-specific composable to request notification permission.
 *
 * Android 13+: Shows system permission dialog
 * Android 12-: No action needed (automatic permission)
 * iOS: To be implemented when needed
 *
 * @param shouldRequest Trigger to request permission
 * @param onPermissionResult Callback with permission result (true if granted)
 */
@Composable
expect fun RequestNotificationPermissionIfNeeded(
    shouldRequest: Boolean,
    onPermissionResult: (Boolean) -> Unit
)
