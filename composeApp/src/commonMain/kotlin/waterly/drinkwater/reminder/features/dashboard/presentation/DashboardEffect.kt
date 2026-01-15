package waterly.drinkwater.reminder.features.dashboard.presentation

import waterly.drinkwater.reminder.core.presentation.UiEffect

sealed interface DashboardUiEffect : UiEffect {
    data object ShowGoalReached : DashboardUiEffect
}