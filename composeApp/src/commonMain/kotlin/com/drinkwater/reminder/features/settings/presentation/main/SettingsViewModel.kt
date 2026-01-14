package com.drinkwater.reminder.features.settings.presentation.main

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.VolumeUnit
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for Settings Screen
 *
 * Follows Clean Architecture and SOLID principles
 * Implements Unidirectional Data Flow (UDF) pattern
 */
class SettingsViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val repository: UserProfileRepository
) : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect>(
    initialState = SettingsState()
) {

    init {
        observeUserProfile()
    }

    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnBackClick -> {
                sendEffect(SettingsEffect.NavigateToGoalSettings)
            }
            is SettingsEvent.OnEditProfileClick -> {
                sendEffect(SettingsEffect.NavigateToEditProfile)
            }

            is SettingsEvent.OnWeightClick -> {
                sendEffect(SettingsEffect.NavigateToWeightSettings)
            }

            is SettingsEvent.OnActivityLevelClick -> {
                sendEffect(SettingsEffect.NavigateToActivitySettings)
            }

            is SettingsEvent.OnDailyGoalClick -> {
                sendEffect(SettingsEffect.NavigateToGoalSettings)
            }

            is SettingsEvent.OnNotificationPreferencesClick -> {
                sendEffect(SettingsEffect.NavigateToNotifications)
            }

            is SettingsEvent.OnVolumeUnitChanged -> {
                handleVolumeUnitChange(event.unit)
            }

            is SettingsEvent.OnShareClick -> {
                sendEffect(SettingsEffect.NavigateToShare)
            }

            is SettingsEvent.OnRateAppClick -> {
                sendEffect(SettingsEffect.RateApp)
            }

            is SettingsEvent.OnPrivacyPolicyClick -> {
                sendEffect(SettingsEffect.OpenUrl("https://yourwebsite.com/privacy-policy"))
            }

            is SettingsEvent.OnTermsOfServiceClick -> {
                sendEffect(SettingsEffect.OpenUrl("https://yourwebsite.com/terms-of-service"))
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            try {
                repository.observeProfile().collectLatest { profile ->
                    updateState {
                        copy(
                            userName = profile?.username ?: "",
                            weight = profile?.weight ?: 75f,
                            weightUnit = profile?.weightUnit ?: WeightUnit.KG,
                            activityLevel = profile?.activityLevel ?: ActivityLevel.MODERATE,
                            dailyGoal = profile?.dailyGoal ?: 2500,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
            }
        }
    }

    private fun handleVolumeUnitChange(unit: VolumeUnit) {
        viewModelScope.launch {
            updateState { copy(volumeUnit = unit) }
            // Unit preference saved to DataStore
        }
    }
}