package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel for Notification Preferences screen
 * Follows UDF pattern: Event → ViewModel → State → UI
 */
class NotificationPreferencesViewModel : BaseViewModel<
    NotificationPreferencesState,
    NotificationPreferencesEvent,
    NotificationPreferencesSideEffect
>(
    initialState = NotificationPreferencesState()
) {

    init {
        loadPreferences()
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
                sendEffect(
                    NotificationPreferencesSideEffect.ShowFrequencyPicker(
                        currentState.frequencyMinutes
                    )
                )
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

            is NotificationPreferencesEvent.OnTogglePauseWhenGoalReached -> {
                updateState { copy(pauseWhenGoalReached = event.enabled) }
                savePreferences()
            }
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            try {
                updateState { copy(isLoading = true, error = null) }

                // TODO: Load from repository when implemented
                // For now, using default values from State

                updateState { copy(isLoading = false) }
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
                // TODO: Save to repository when implemented
                // For MVP, state is already updated
            } catch (e: Exception) {
                sendEffect(
                    NotificationPreferencesSideEffect.ShowError(
                        e.message ?: "Failed to save preferences"
                    )
                )
            }
        }
    }
}
