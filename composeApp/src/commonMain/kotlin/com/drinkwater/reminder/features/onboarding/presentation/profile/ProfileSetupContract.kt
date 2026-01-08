package com.drinkwater.reminder.features.onboarding.profile

import com.drinkwater.reminder.core.presentation.UiEffect
import com.drinkwater.reminder.core.presentation.UiEvent
import com.drinkwater.reminder.core.presentation.UiState
import com.drinkwater.reminder.core.ui.components.BiologicalSex

/**
 * Age group options
 */
enum class AgeGroup {
    AGE_18_30,
    AGE_31_50,
    AGE_51_60,
    AGE_60_PLUS
}

/**
 * Activity level options
 */
enum class ActivityLevel {
    SEDENTARY,      // 1
    LIGHT,          // 2
    MODERATE,       // 3
    ACTIVE,         // 4
    VERY_ACTIVE     // 5
}

/**
 * Weight unit
 */
enum class WeightUnit {
    KG,
    LBS
}

/**
 * UI State for Profile Setup Screen
 */
data class ProfileSetupUiState(
    val biologicalSex: BiologicalSex = BiologicalSex.FEMALE,
    val ageGroup: AgeGroup = AgeGroup.AGE_31_50,
    val weight: String = "65",
    val weightUnit: WeightUnit = WeightUnit.KG,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val isCalculating: Boolean = false
) : UiState

/**
 * UI Events for Profile Setup Screen
 */
sealed interface ProfileSetupUiEvent : UiEvent {
    data class OnBiologicalSexSelected(val sex: BiologicalSex) : ProfileSetupUiEvent
    data class OnAgeGroupSelected(val ageGroup: AgeGroup) : ProfileSetupUiEvent
    data class OnWeightChanged(val weight: String) : ProfileSetupUiEvent
    data class OnWeightUnitChanged(val unit: WeightUnit) : ProfileSetupUiEvent
    data class OnActivityLevelChanged(val level: ActivityLevel) : ProfileSetupUiEvent
    data object OnCalculateGoalClick : ProfileSetupUiEvent
}

/**
 * UI Effects for Profile Setup Screen
 */
sealed interface ProfileSetupUiEffect : UiEffect {
    data class NavigateToDashboard(val dailyGoal: Int) : ProfileSetupUiEffect
}
