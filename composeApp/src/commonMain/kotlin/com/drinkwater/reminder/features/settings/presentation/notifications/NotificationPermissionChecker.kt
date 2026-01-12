package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.compose.runtime.Composable

/**
 * Platform-specific function to check if notification permission is granted.
 *
 * @return true if permission is granted, false otherwise
 */
@Composable
expect fun CheckNotificationPermission(): Boolean
