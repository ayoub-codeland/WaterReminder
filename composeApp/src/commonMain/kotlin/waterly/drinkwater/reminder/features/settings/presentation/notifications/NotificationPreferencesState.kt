package waterly.drinkwater.reminder.features.settings.presentation.notifications

import waterly.drinkwater.reminder.core.presentation.UiState

/**
 * Immutable state for Notification Preferences screen
 */
data class NotificationPreferencesState(
    val isEnabled: Boolean = true,
    val frequencyMinutes: Int = 60,
    val wakeUpTime: String = "07:00 AM",
    val bedtime: String = "10:30 PM",
    val pauseWhenGoalReached: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState
