package com.drinkwater.reminder.features.settings.presentation.goal

import com.drinkwater.reminder.core.presentation.UiState

data class UpdateDailyGoalState(
    val currentGoal: Int = 2500,
    val recommendedGoal: Int = 2400,
    val minGoal: Int = 500,
    val maxGoal: Int = 5000,
    val adjustmentStep: Int = 50,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false
) : UiState