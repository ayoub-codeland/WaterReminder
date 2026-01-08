package com.drinkwater.reminder.features.settings.presentation.goal

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class UpdateDailyGoalViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val calculateDailyGoal: CalculateDailyGoalUseCase,
    private val repository: UserProfileRepository
) : BaseViewModel<UpdateDailyGoalState, UpdateDailyGoalEvent, UpdateDailyGoalEffect>(
    initialState = UpdateDailyGoalState(isLoading = true)
) {

    private var isSavingInProgress = false

    init {
        loadCurrentGoal()
    }

    override fun onEvent(event: UpdateDailyGoalEvent) {
        when (event) {
            is UpdateDailyGoalEvent.OnGoalChanged -> {
                val clampedGoal = event.goal.coerceIn(currentState.minGoal, currentState.maxGoal)
                updateState { copy(currentGoal = clampedGoal) }
            }
            is UpdateDailyGoalEvent.OnIncrementGoal -> {
                val newGoal = (currentState.currentGoal + currentState.adjustmentStep)
                    .coerceAtMost(currentState.maxGoal)
                updateState { copy(currentGoal = newGoal) }
            }
            is UpdateDailyGoalEvent.OnDecrementGoal -> {
                val newGoal = (currentState.currentGoal - currentState.adjustmentStep)
                    .coerceAtLeast(currentState.minGoal)
                updateState { copy(currentGoal = newGoal) }
            }
            is UpdateDailyGoalEvent.OnSaveClick -> {
                saveGoal()
            }
            is UpdateDailyGoalEvent.OnCancelClick -> {
                sendEffect(UpdateDailyGoalEffect.NavigateBack)
            }
            is UpdateDailyGoalEvent.OnBackClick -> {
                sendEffect(UpdateDailyGoalEffect.NavigateBack)
            }
        }
    }

    private fun loadCurrentGoal() {
        viewModelScope.launch {
            try {
                val profile = getUserProfile()
                val currentGoal = repository.getDailyGoal() ?: 2500
                val recommendedGoal = if (profile != null) {
                    calculateDailyGoal(profile)
                } else {
                    2400
                }

                updateState {
                    copy(
                        currentGoal = currentGoal,
                        recommendedGoal = recommendedGoal,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(UpdateDailyGoalEffect.ShowError("Failed to load data"))
            }
        }
    }

    private fun saveGoal() {
        if (isSavingInProgress) return
        isSavingInProgress = true
        
        updateState { copy(isSaving = true) }

        viewModelScope.launch {
            try {
                repository.saveDailyGoal(currentState.currentGoal)
                updateState { copy(isSaving = false) }
                sendEffect(UpdateDailyGoalEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isSaving = false) }
                isSavingInProgress = false
                sendEffect(UpdateDailyGoalEffect.ShowError(e.message ?: "Failed to save"))
            }
        }
    }
}
