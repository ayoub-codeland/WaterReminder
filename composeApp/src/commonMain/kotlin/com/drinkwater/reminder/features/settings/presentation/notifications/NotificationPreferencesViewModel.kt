package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.NotificationPreference
import com.drinkwater.reminder.core.domain.usecase.notification.GetNotificationPreferencesUseCase
import com.drinkwater.reminder.core.domain.usecase.notification.ObserveNotificationPreferencesUseCase
import com.drinkwater.reminder.core.domain.usecase.notification.SaveNotificationPreferencesUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for Notification Preferences screen
 * Follows UDF pattern: Event → ViewModel → State → UI
 *
 * Now integrated with NotificationRepository for actual notification scheduling!
 */
class NotificationPreferencesViewModel(
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val saveNotificationPreferencesUseCase: SaveNotificationPreferencesUseCase,
    private val observeNotificationPreferencesUseCase: ObserveNotificationPreferencesUseCase
) : BaseViewModel<
    NotificationPreferencesState,
    NotificationPreferencesEvent,
    NotificationPreferencesSideEffect
>(
    initialState = NotificationPreferencesState()
) {

    init {
        loadPreferences()
        observePreferences()
    }

    override fun onEvent(event: NotificationPreferencesEvent) {
        when (event) {
            is NotificationPreferencesEvent.OnBackClick -> {
                sendEffect(NotificationPreferencesSideEffect.NavigateBack)
            }

            is NotificationPreferencesEvent.OnToggleNotifications -> {
                updateState { copy(isEnabled = event.enabled) }
                savePreferences()
            }

            is NotificationPreferencesEvent.OnFrequencyClick -> {
                sendEffect(NotificationPreferencesSideEffect.NavigateToUpdateNotificationSchedule)
            }

            is NotificationPreferencesEvent.OnFrequencySelected -> {
                updateState { copy(frequencyMinutes = event.frequencyMinutes) }
                savePreferences()
            }

            is NotificationPreferencesEvent.OnSaveFrequencyAndNavigateBack -> {
                updateState { copy(frequencyMinutes = event.frequencyMinutes) }
                savePreferencesAndNavigateBack()
            }

            is NotificationPreferencesEvent.OnWakeUpTimeClick -> {
                sendEffect(
                    NotificationPreferencesSideEffect.ShowTimePicker(
                        currentTime = currentState.wakeUpTime,
                        isWakeUpTime = true
                    )
                )
            }

            is NotificationPreferencesEvent.OnBedtimeClick -> {
                sendEffect(
                    NotificationPreferencesSideEffect.ShowTimePicker(
                        currentTime = currentState.bedtime,
                        isWakeUpTime = false
                    )
                )
            }

            is NotificationPreferencesEvent.OnWakeUpTimeSelected -> {
                updateState { copy(wakeUpTime = event.time) }
                savePreferences()
            }

            is NotificationPreferencesEvent.OnBedtimeSelected -> {
                updateState { copy(bedtime = event.time) }
                savePreferences()
            }

            is NotificationPreferencesEvent.OnTogglePauseWhenGoalReached -> {
                updateState { copy(pauseWhenGoalReached = event.enabled) }
                savePreferences()
            }
        }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            observeNotificationPreferencesUseCase().collectLatest { preference ->
                preference?.let {
                    updateState {
                        copy(
                            isEnabled = it.isEnabled,
                            frequencyMinutes = it.frequencyMinutes,
                            wakeUpTime = convertTo12HourFormat(it.wakeUpTime),
                            bedtime = convertTo12HourFormat(it.bedtime),
                            pauseWhenGoalReached = it.pauseWhenGoalReached
                        )
                    }
                }
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                updateState { copy(isLoading = true, error = null) }

                val preference = getNotificationPreferencesUseCase()
                if (preference != null) {
                    updateState {
                        copy(
                            isEnabled = preference.isEnabled,
                            frequencyMinutes = preference.frequencyMinutes,
                            wakeUpTime = convertTo12HourFormat(preference.wakeUpTime),
                            bedtime = convertTo12HourFormat(preference.bedtime),
                            pauseWhenGoalReached = preference.pauseWhenGoalReached,
                            isLoading = false
                        )
                    }
                } else {
                    // No preferences saved yet, use defaults
                    updateState { copy(isLoading = false) }
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load preferences"
                    )
                }
                sendEffect(
                    NotificationPreferencesSideEffect.ShowError(
                        e.message ?: "Failed to load preferences"
                    )
                )
            }
        }
    }

    private fun savePreferences() {
        viewModelScope.launch {
            try {
                val preference = NotificationPreference(
                    isEnabled = currentState.isEnabled,
                    frequencyMinutes = currentState.frequencyMinutes,
                    wakeUpTime = convertTo24HourFormat(currentState.wakeUpTime),
                    bedtime = convertTo24HourFormat(currentState.bedtime),
                    pauseWhenGoalReached = currentState.pauseWhenGoalReached
                )

                saveNotificationPreferencesUseCase(preference)
            } catch (e: Exception) {
                sendEffect(
                    NotificationPreferencesSideEffect.ShowError(
                        e.message ?: "Failed to save preferences"
                    )
                )
            }
        }
    }

    /**
     * Save preferences and navigate back only after save completes.
     * Ensures data is persisted before navigation.
     */
    private fun savePreferencesAndNavigateBack() {
        viewModelScope.launch {
            try {
                val preference = NotificationPreference(
                    isEnabled = currentState.isEnabled,
                    frequencyMinutes = currentState.frequencyMinutes,
                    wakeUpTime = convertTo24HourFormat(currentState.wakeUpTime),
                    bedtime = convertTo24HourFormat(currentState.bedtime),
                    pauseWhenGoalReached = currentState.pauseWhenGoalReached
                )

                // Wait for save to complete
                saveNotificationPreferencesUseCase(preference)

                // Only navigate back after successful save
                sendEffect(NotificationPreferencesSideEffect.NavigateBack)
            } catch (e: Exception) {
                sendEffect(
                    NotificationPreferencesSideEffect.ShowError(
                        e.message ?: "Failed to save preferences"
                    )
                )
            }
        }
    }

    /**
     * Convert 24-hour format to 12-hour format with AM/PM
     * Example: "07:00" -> "07:00 AM", "14:30" -> "02:30 PM"
     */
    private fun convertTo12HourFormat(time24: String): String {
        val parts = time24.split(":")
        if (parts.size != 2) return time24

        val hour = parts[0].toIntOrNull() ?: return time24
        val minute = parts[1]

        return when {
            hour == 0 -> "12:$minute AM"
            hour < 12 -> "${hour.toString().padStart(2, '0')}:$minute AM"
            hour == 12 -> "12:$minute PM"
            else -> "${(hour - 12).toString().padStart(2, '0')}:$minute PM"
        }
    }

    /**
     * Convert 12-hour format with AM/PM to 24-hour format
     * Example: "07:00 AM" -> "07:00", "02:30 PM" -> "14:30"
     */
    private fun convertTo24HourFormat(time12: String): String {
        val parts = time12.split(" ")
        if (parts.size != 2) return time12

        val timeParts = parts[0].split(":")
        if (timeParts.size != 2) return time12

        val hour = timeParts[0].toIntOrNull() ?: return time12
        val minute = timeParts[1]
        val amPm = parts[1]

        val hour24 = when {
            amPm == "AM" && hour == 12 -> 0
            amPm == "AM" -> hour
            amPm == "PM" && hour == 12 -> 12
            amPm == "PM" -> hour + 12
            else -> hour
        }

        return "${hour24.toString().padStart(2, '0')}:$minute"
    }
}
