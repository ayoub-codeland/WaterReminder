package com.drinkwater.reminder.features.settings.presentation.activity

import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

/**
 * ViewModel for Update Activity Level Screen
 */
class UpdateActivityLevelViewModel(
    initialLevel: ActivityLevel = ActivityLevel.MODERATE
) : BaseViewModel<UpdateActivityLevelUiState, UpdateActivityLevelUiEvent, UpdateActivityLevelUiEffect>(
    initialState = UpdateActivityLevelUiState(activityLevel = initialLevel)
) {

    override fun onEvent(event: UpdateActivityLevelUiEvent) {
        when (event) {
            is UpdateActivityLevelUiEvent.OnActivityLevelChanged -> {
                updateState { copy(activityLevel = event.level) }
            }
            
            is UpdateActivityLevelUiEvent.OnSaveClick -> {
                saveActivityLevel()
            }
            
            is UpdateActivityLevelUiEvent.OnCancelClick -> {
                sendEffect(UpdateActivityLevelUiEffect.NavigateBack)
            }
            
            is UpdateActivityLevelUiEvent.OnBackClick -> {
                sendEffect(UpdateActivityLevelUiEffect.NavigateBack)
            }
        }
    }

    private fun saveActivityLevel() {
        updateState { copy(isSaving = true) }
        
        viewModelScope.launch {
            // TODO: Save activity level to repository
            kotlinx.coroutines.delay(500)
            
            updateState { copy(isSaving = false) }
            sendEffect(UpdateActivityLevelUiEffect.NavigateBack)
        }
    }
}
