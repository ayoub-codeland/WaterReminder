package com.drinkwater.reminder.features.settings.presentation.weight

import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.presentation.UiState

data class UpdateWeightState(
    val weight: Float = 75.5f,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val minWeight: Float = 30f,
    val maxWeight: Float = 150f,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false
) : UiState