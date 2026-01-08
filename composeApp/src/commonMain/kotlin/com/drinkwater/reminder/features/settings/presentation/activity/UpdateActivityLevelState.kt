package com.drinkwater.reminder.features.settings.presentation.activity

import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.presentation.UiState

data class UpdateActivityLevelState(
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false
) : UiState {
    val displayName: String
        get() = activityLevel.displayName

    val description: String
        get() = activityLevel.description

    val additionalWater: Int
        get() = activityLevel.additionalWaterMl

    val sliderValue: Float
        get() = activityLevel.toSliderValue()
}