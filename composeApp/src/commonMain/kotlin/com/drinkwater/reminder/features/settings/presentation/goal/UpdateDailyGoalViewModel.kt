package com.drinkwater.reminder.features.settings.presentation.goal

import com.drinkwater.reminder.core.presentation.BaseViewModel

/**
 * ViewModel for Update Daily Goal Screen
 */
class UpdateDailyGoalViewModel(
    initialGoal: Int = 2500,
    recommendedGoal: Int = 2400
) : BaseViewModel<UpdateDailyGoalUiState, UpdateDailyGoalUiEvent, UpdateDailyGoalUiEffect>(
    initialState = UpdateDailyGoalUiState(currentGoal = initialGoal, recommendedGoal = recommendedGoal)
) {

    override fun onEvent(event: UpdateDailyGoalUiEvent) {
        when (event) {
            is UpdateDailyGoalUiEvent.OnGoalChanged -> {
                val clampedGoal = event.goal.coerceIn(currentState.minGoal, currentState.maxGoal)
                updateState { copy(currentGoal = clampedGoal) }
            }
            
            is UpdateDailyGoalUiEvent.OnIncrementGoal -> {
                val newGoal = (currentState.currentGoal + currentState.adjustmentStep).coerceAtMost(currentState.maxGoal)
                updateState { copy(currentGoal = newGoal) }
            }
            
            is UpdateDailyGoalUiEvent.OnDecrementGoal -> {
                val newGoal = (currentState.currentGoal - currentState.adjustmentStep).coerceAtLeast(currentState.minGoal)
                updateState { copy(currentGoal = newGoal) }
            }
            
            is UpdateDailyGoalUiEvent.OnSaveClick -> {
                updateState { copy(isSaving = true) }
                viewModelScope.launch {
                    kotlinx.coroutines.delay(500)
                    updateState { copy(isSaving = false) }
                    sendEffect(UpdateDailyGoalUiEffect.NavigateBack)
                }
            }
            
            is UpdateDailyGoalUiEvent.OnCancelClick -> {
                sendEffect(UpdateDailyGoalUiEffect.NavigateBack)
            }
            
            is UpdateDailyGoalUiEvent.OnBackClick -> {
                sendEffect(UpdateDailyGoalUiEffect.NavigateBack)
            }
        }
    }
}
