package waterly.drinkwater.reminder.features.dashboard.presentation

import waterly.drinkwater.reminder.core.presentation.UiState

data class DashboardUiState(
    val dailyGoal: Int = 2500,
    val currentIntake: Int = 0,
    val progressPercentage: Float = 0f
) : UiState