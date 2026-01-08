package com.drinkwater.reminder.features.onboarding.presentation.profile

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile Setup Screen
 */
class ProfileSetupViewModel : BaseViewModel<ProfileSetupUiState, ProfileSetupUiEvent, ProfileSetupUiEffect>(
    initialState = ProfileSetupUiState()
) {
    override fun onEvent(event: ProfileSetupUiEvent) {
        when (event) {
            is ProfileSetupUiEvent.OnBiologicalSexSelected -> {
                updateState { copy(biologicalSex = event.sex) }
            }
            is ProfileSetupUiEvent.OnAgeGroupSelected -> {
                updateState { copy(ageGroup = event.ageGroup) }
            }
            is ProfileSetupUiEvent.OnWeightChanged -> {
                if (event.weight.isEmpty() || event.weight.toIntOrNull() != null) {
                    updateState { copy(weight = event.weight) }
                }
            }
            is ProfileSetupUiEvent.OnWeightUnitChanged -> {
                updateState { copy(weightUnit = event.unit) }
            }
            is ProfileSetupUiEvent.OnActivityLevelChanged -> {
                updateState { copy(activityLevel = event.level) }
            }
            is ProfileSetupUiEvent.OnCalculateGoalClick -> handleCalculateGoal()
        }
    }
    
    private fun handleCalculateGoal() {
        val weight = currentState.weight.toIntOrNull()
        if (weight == null || weight <= 0) return
        
        updateState { copy(isCalculating = true) }
        
        viewModelScope.launch {
            delay(300)
            
            // Convert to kg if needed
            val weightInKg = if (currentState.weightUnit == WeightUnit.LBS) {
                (weight * 0.453592).toInt()
            } else {
                weight
            }
            
            // Calculate based on activity level
            val baseAmount = weightInKg * 30
            val activityMultiplier = when (currentState.activityLevel) {
                ActivityLevel.SEDENTARY -> 1.0
                ActivityLevel.LIGHT -> 1.1
                ActivityLevel.MODERATE -> 1.2
                ActivityLevel.ACTIVE -> 1.3
                ActivityLevel.VERY_ACTIVE -> 1.5
            }
            
            val dailyGoal = (baseAmount * activityMultiplier).toInt()
            
            updateState { copy(isCalculating = false) }
            sendEffect(ProfileSetupUiEffect.NavigateToDashboard(dailyGoal))
        }
    }
}
