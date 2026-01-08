package com.drinkwater.reminder.features.settings.presentation.weight

import com.drinkwater.reminder.core.presentation.BaseViewModel
import com.drinkwater.reminder.features.onboarding.presentation.profile.WeightUnit

/**
 * ViewModel for Update Weight Screen
 * 
 * Handles weight input, unit conversion, and slider synchronization
 */
class UpdateWeightViewModel(
    initialWeight: Float = 75.5f,
    initialUnit: WeightUnit = WeightUnit.KG
) : BaseViewModel<UpdateWeightUiState, UpdateWeightUiEvent, UpdateWeightUiEffect>(
    initialState = UpdateWeightUiState(
        weight = initialWeight,
        weightUnit = initialUnit
    )
) {

    override fun onEvent(event: UpdateWeightUiEvent) {
        when (event) {
            is UpdateWeightUiEvent.OnWeightChanged -> {
                handleWeightChange(event.weight)
            }
            
            is UpdateWeightUiEvent.OnWeightUnitChanged -> {
                handleUnitChange(event.unit)
            }
            
            is UpdateWeightUiEvent.OnIncrementWeight -> {
                incrementWeight()
            }
            
            is UpdateWeightUiEvent.OnDecrementWeight -> {
                decrementWeight()
            }
            
            is UpdateWeightUiEvent.OnSaveClick -> {
                saveWeight()
            }
            
            is UpdateWeightUiEvent.OnBackClick -> {
                sendEffect(UpdateWeightUiEffect.NavigateBack)
            }
        }
    }

    private fun handleWeightChange(weight: Float) {
        val clampedWeight = weight.coerceIn(currentState.minWeight, currentState.maxWeight)
        updateState {
            copy(weight = clampedWeight)
        }
    }

    private fun handleUnitChange(unit: WeightUnit) {
        if (unit == currentState.weightUnit) return
        
        val convertedWeight = when (unit) {
            WeightUnit.KG -> currentState.weight / 2.20462f // lbs to kg
            WeightUnit.LBS -> currentState.weight * 2.20462f // kg to lbs
        }
        
        val (newMin, newMax) = when (unit) {
            WeightUnit.KG -> 30f to 150f
            WeightUnit.LBS -> 66f to 330f
        }
        
        updateState {
            copy(
                weight = convertedWeight,
                weightUnit = unit,
                minWeight = newMin,
                maxWeight = newMax
            )
        }
    }

    private fun incrementWeight() {
        val increment = if (currentState.weightUnit == WeightUnit.KG) 0.5f else 1f
        val newWeight = (currentState.weight + increment).coerceAtMost(currentState.maxWeight)
        updateState {
            copy(weight = newWeight)
        }
    }

    private fun decrementWeight() {
        val decrement = if (currentState.weightUnit == WeightUnit.KG) 0.5f else 1f
        val newWeight = (currentState.weight - decrement).coerceAtLeast(currentState.minWeight)
        updateState {
            copy(weight = newWeight)
        }
    }

    private fun saveWeight() {
        updateState { copy(isSaving = true) }
        
        viewModelScope.launch {
            // TODO: Save weight to repository
            kotlinx.coroutines.delay(500) // Simulate save
            
            updateState { copy(isSaving = false) }
            sendEffect(UpdateWeightUiEffect.NavigateBack)
        }
    }
}
