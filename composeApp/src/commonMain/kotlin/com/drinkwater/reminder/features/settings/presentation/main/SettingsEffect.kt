package com.drinkwater.reminder.features.settings.presentation.main

import com.drinkwater.reminder.core.presentation.UiEffect

sealed interface SettingsEffect : UiEffect {
    data object NavigateBack : SettingsEffect
    data object NavigateToEditProfile : SettingsEffect
    data object NavigateToWeightSettings : SettingsEffect
    data object NavigateToActivitySettings : SettingsEffect
    data object NavigateToGoalSettings : SettingsEffect
    data object NavigateToNotifications : SettingsEffect
    data object ShareApp : SettingsEffect
    data object RateApp : SettingsEffect
    data class OpenUrl(val url: String) : SettingsEffect
}