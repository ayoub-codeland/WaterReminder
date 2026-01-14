package com.drinkwater.reminder.features.onboarding.presentation.profile

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase
import com.drinkwater.reminder.core.domain.usecase.SaveUserProfileUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile Setup Screen
 * Saves user profile after calculation
 */
class ProfileSetupViewModel(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val calculateDailyGoalUseCase: CalculateDailyGoalUseCase
) : BaseViewModel<ProfileSetupUiState, ProfileSetupUiEvent, ProfileSetupUiEffect>(
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
        val weight = currentState.weight.toFloatOrNull()
        if (weight == null || weight <= 0) return

        updateState { copy(isCalculating = true) }
        viewModelScope.launch {
            delay(300)

            // Map UI BiologicalSex to Domain BiologicalSex
            val domainBiologicalSex = when (currentState.biologicalSex) {
                com.drinkwater.reminder.core.ui.components.BiologicalSex.MALE ->
                    com.drinkwater.reminder.core.domain.model.BiologicalSex.MALE
                com.drinkwater.reminder.core.ui.components.BiologicalSex.FEMALE ->
                    com.drinkwater.reminder.core.domain.model.BiologicalSex.FEMALE
            }

            // Create user profile for calculation
            val profile = UserProfile(
                biologicalSex = domainBiologicalSex,
                ageGroup = currentState.ageGroup,
                weight = weight,
                weightUnit = currentState.weightUnit,
                activityLevel = currentState.activityLevel,
                dailyGoal = 0, // Will be calculated
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            // Calculate daily goal using the use case
            val dailyGoal = calculateDailyGoalUseCase(profile)

            // Update profile with calculated goal
            val profileWithGoal = profile.copy(dailyGoal = dailyGoal)

            // Save profile to repository
            saveUserProfileUseCase(profileWithGoal)

            updateState { copy(isCalculating = false) }
            sendEffect(ProfileSetupUiEffect.NavigateToDashboard(dailyGoal))
        }
    }
}
