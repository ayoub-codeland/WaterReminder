package com.drinkwater.reminder.features.settings.presentation.weight

import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.presentation.UiEvent

sealed interface UpdateWeightEvent : UiEvent {
    data class OnWeightChanged(val weight: Float) : UpdateWeightEvent
    data class OnWeightUnitChanged(val unit: WeightUnit) : UpdateWeightEvent
    data object OnIncrementWeight : UpdateWeightEvent
    data object OnDecrementWeight : UpdateWeightEvent
    data object OnSaveClick : UpdateWeightEvent
    data object OnBackClick : UpdateWeightEvent
}