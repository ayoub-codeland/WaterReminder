package com.drinkwater.reminder.features.settings.presentation.notifications

import com.drinkwater.reminder.core.presentation.UiEffect

/**
 * Side effects for Notification Preferences screen
 */
sealed interface NotificationPreferencesSideEffect : UiEffect {
    data object NavigateBack : NotificationPreferencesSideEffect
    data object NavigateToUpdateNotificationSchedule : NotificationPreferencesSideEffect
    data class ShowError(val message: String) : NotificationPreferencesSideEffect
    data class ShowFrequencyPicker(val currentMinutes: Int) : NotificationPreferencesSideEffect
    data class ShowTimePicker(val currentTime: String, val isWakeUpTime: Boolean) : NotificationPreferencesSideEffect
}
