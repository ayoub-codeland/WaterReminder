package com.drinkwater.reminder.features.settings.presentation.activity

import com.drinkwater.reminder.core.presentation.UiEffect

sealed interface UpdateActivityLevelEffect : UiEffect {
    data object NavigateBack : UpdateActivityLevelEffect
    data class ShowError(val message: String) : UpdateActivityLevelEffect
}
