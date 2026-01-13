package com.drinkwater.reminder.features.settings.presentation.main

import com.drinkwater.reminder.core.domain.model.VolumeUnit
import com.drinkwater.reminder.core.presentation.UiEvent

sealed interface SettingsEvent : UiEvent {
    data object OnEditProfileClick : SettingsEvent
    data object OnWeightClick : SettingsEvent
    data object OnActivityLevelClick : SettingsEvent
    data object OnDailyGoalClick : SettingsEvent
    data object OnNotificationPreferencesClick : SettingsEvent
    data class OnVolumeUnitChanged(val unit: VolumeUnit) : SettingsEvent
    data object OnShareClick : SettingsEvent
    data object OnRateAppClick : SettingsEvent
    data object OnPrivacyPolicyClick : SettingsEvent
    data object OnTermsOfServiceClick : SettingsEvent
    data object OnBackClick : SettingsEvent
}