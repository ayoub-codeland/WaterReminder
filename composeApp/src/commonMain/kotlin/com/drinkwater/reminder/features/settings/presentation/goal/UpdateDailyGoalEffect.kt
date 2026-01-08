package com.drinkwater.reminder.features.settings.presentation.goal

import com.drinkwater.reminder.core.presentation.UiEffect

sealed interface UpdateDailyGoalEffect : UiEffect {
    data object NavigateBack : UpdateDailyGoalEffect
    data class ShowError(val message: String) : UpdateDailyGoalEffect
}
