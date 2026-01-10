package com.drinkwater.reminder.features.settings.presentation.notifications

import com.drinkwater.reminder.core.presentation.UiEvent

/**
 * User events for Notification Preferences screen
 */
sealed interface NotificationPreferencesEvent : UiEvent {
    data object OnBackClick : NotificationPreferencesEvent
    data class OnToggleNotifications(val enabled: Boolean) : NotificationPreferencesEvent
    data object OnFrequencyClick : NotificationPreferencesEvent
    data object OnWakeUpTimeClick : NotificationPreferencesEvent
    data object OnBedtimeClick : NotificationPreferencesEvent
    data class OnTogglePauseWhenGoalReached(val enabled: Boolean) : NotificationPreferencesEvent
}
