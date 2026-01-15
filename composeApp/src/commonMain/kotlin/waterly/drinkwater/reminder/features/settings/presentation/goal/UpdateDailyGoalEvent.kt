package waterly.drinkwater.reminder.features.settings.presentation.goal

import waterly.drinkwater.reminder.core.presentation.UiEvent

sealed interface UpdateDailyGoalEvent : UiEvent {
    data class OnGoalChanged(val goal: Int) : UpdateDailyGoalEvent
    data object OnIncrementGoal : UpdateDailyGoalEvent
    data object OnDecrementGoal : UpdateDailyGoalEvent
    data object OnSaveClick : UpdateDailyGoalEvent
    data object OnCancelClick : UpdateDailyGoalEvent
    data object OnBackClick : UpdateDailyGoalEvent
}
