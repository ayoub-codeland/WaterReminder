package waterly.drinkwater.reminder.features.dashboard.presentation

import waterly.drinkwater.reminder.core.presentation.UiEvent

sealed interface DashboardUiEvent : UiEvent {
    data class OnAddWater(val amount: Int) : DashboardUiEvent
}