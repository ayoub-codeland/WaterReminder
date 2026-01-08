package com.drinkwater.reminder.features.settings

import com.drinkwater.reminder.core.presentation.BaseViewModel

/**
 * ViewModel for Settings Screen
 *
 * Follows Clean Architecture and SOLID principles
 * Implements Unidirectional Data Flow (UDF) pattern
 */
class SettingsViewModel : BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsUiEffect>(
    initialState = SettingsUiState()
) {

    override fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnEditProfileClick -> {
                sendEffect(SettingsUiEffect.NavigateToEditProfile)
            }

            is SettingsUiEvent.OnWeightClick -> {
                sendEffect(SettingsUiEffect.NavigateToWeightSettings)
            }

            is SettingsUiEvent.OnActivityLevelClick -> {
                sendEffect(SettingsUiEffect.NavigateToActivitySettings)
            }

            is SettingsUiEvent.OnDailyGoalClick -> {
                sendEffect(SettingsUiEffect.NavigateToGoalSettings)
            }

            is SettingsUiEvent.OnNotificationPreferencesClick -> {
                sendEffect(SettingsUiEffect.NavigateToNotifications)
            }

            is SettingsUiEvent.OnVolumeUnitChanged -> {
                handleVolumeUnitChange(event.unit)
            }

            is SettingsUiEvent.OnStartOfWeekClick -> {
                sendEffect(SettingsUiEffect.NavigateToStartOfWeek)
            }

            is SettingsUiEvent.OnPrivacyPolicyClick -> {
                sendEffect(SettingsUiEffect.OpenUrl("https://example.com/privacy"))
            }

            is SettingsUiEvent.OnTermsOfServiceClick -> {
                sendEffect(SettingsUiEffect.OpenUrl("https://example.com/terms"))
            }
        }
    }

    private fun handleVolumeUnitChange(unit: VolumeUnit) {
        updateState {
            copy(volumeUnit = unit)
        }
    }
}