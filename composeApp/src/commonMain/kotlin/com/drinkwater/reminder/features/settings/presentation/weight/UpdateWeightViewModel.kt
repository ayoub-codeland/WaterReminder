package com.drinkwater.reminder.features.settings.presentation.weight

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import com.drinkwater.reminder.features.settings.domain.usecase.UpdateWeightUseCase
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class UpdateWeightViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val updateWeight: UpdateWeightUseCase
) : BaseViewModel<UpdateWeightState, UpdateWeightEvent, UpdateWeightEffect>(
    initialState = UpdateWeightState(isLoading = true)
) {

    private var isSavingInProgress = false

    init {
        loadCurrentWeight()
    }

    override fun onEvent(event: UpdateWeightEvent) {
        when (event) {
            is UpdateWeightEvent.OnWeightChanged -> {
                handleWeightChange(event.weight)
            }
            is UpdateWeightEvent.OnWeightUnitChanged -> {
                handleUnitChange(event.unit)
            }
            is UpdateWeightEvent.OnIncrementWeight -> {
                incrementWeight()
            }
            is UpdateWeightEvent.OnDecrementWeight -> {
                decrementWeight()
            }
            is UpdateWeightEvent.OnSaveClick -> {
                saveWeight()
            }
            is UpdateWeightEvent.OnBackClick -> {
                sendEffect(UpdateWeightEffect.NavigateBack)
            }
        }
    }

    private fun loadCurrentWeight() {
        viewModelScope.launch {
            try {
                val profile = getUserProfile()
                updateState {
                    copy(
                        weight = profile?.weight ?: 75f,
                        weightUnit = profile?.weightUnit ?: WeightUnit.KG,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(UpdateWeightEffect.ShowError("Failed to load data"))
            }
        }
    }

    private fun handleWeightChange(weight: Float) {
        val roundedWeight = (weight * 10).roundToInt() / 10f
        val clampedWeight = roundedWeight.coerceIn(currentState.minWeight, currentState.maxWeight)
        updateState { copy(weight = clampedWeight) }
    }

    private fun handleUnitChange(unit: WeightUnit) {
        if (unit == currentState.weightUnit) return

        val convertedWeight = when (unit) {
            WeightUnit.KG -> currentState.weightUnit.toKg(currentState.weight)
            WeightUnit.LBS -> currentState.weightUnit.toLbs(currentState.weight)
        }

        val roundedWeight = (convertedWeight * 10).roundToInt() / 10f

        val (newMin, newMax) = when (unit) {
            WeightUnit.KG -> 30f to 150f
            WeightUnit.LBS -> 66f to 330f
        }

        updateState {
            copy(
                weight = roundedWeight,
                weightUnit = unit,
                minWeight = newMin,
                maxWeight = newMax
            )
        }
    }

    private fun incrementWeight() {
        val increment = 0.1f
        val newWeight = (currentState.weight + increment).coerceAtMost(currentState.maxWeight)
        val roundedWeight = (newWeight * 10).roundToInt() / 10f
        updateState { copy(weight = roundedWeight) }
    }

    private fun decrementWeight() {
        val decrement = 0.1f
        val newWeight = (currentState.weight - decrement).coerceAtLeast(currentState.minWeight)
        val roundedWeight = (newWeight * 10).roundToInt() / 10f
        updateState { copy(weight = roundedWeight) }
    }

    private fun saveWeight() {
        if (isSavingInProgress) return
        isSavingInProgress = true
        
        updateState { copy(isSaving = true) }

        viewModelScope.launch {
            try {
                updateWeight(currentState.weight, currentState.weightUnit)
                updateState { copy(isSaving = false) }
                sendEffect(UpdateWeightEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isSaving = false) }
                isSavingInProgress = false
                sendEffect(UpdateWeightEffect.ShowError(e.message ?: "Failed to save"))
            }
        }
    }
}
