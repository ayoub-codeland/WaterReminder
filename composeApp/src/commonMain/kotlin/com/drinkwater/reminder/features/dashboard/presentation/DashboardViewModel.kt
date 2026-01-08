package com.drinkwater.reminder.features.dashboard

import com.drinkwater.reminder.core.presentation.BaseViewModel

/**
 * ViewModel for Dashboard Screen
 * 
 * Manages daily water intake tracking.
 */
class DashboardViewModel(
    dailyGoal: Int
) : BaseViewModel<DashboardUiState, DashboardUiEvent, DashboardUiEffect>(
    initialState = DashboardUiState(dailyGoal = dailyGoal)
) {
    override fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.OnAddWater -> handleAddWater(event.amount)
        }
    }
    
    private fun handleAddWater(amount: Int) {
        val newIntake = (currentState.currentIntake + amount).coerceAtMost(currentState.dailyGoal)
        val percentage = (newIntake.toFloat() / currentState.dailyGoal.toFloat()).coerceIn(0f, 1f)
        
        updateState {
            copy(
                currentIntake = newIntake,
                progressPercentage = percentage
            )
        }
        
        // Check if goal reached
        if (newIntake >= currentState.dailyGoal && currentState.currentIntake < currentState.dailyGoal) {
            sendEffect(DashboardUiEffect.ShowGoalReached)
        }
    }
}
