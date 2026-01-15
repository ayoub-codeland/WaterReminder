package waterly.drinkwater.reminder.features.settings.presentation.main

import waterly.drinkwater.reminder.core.domain.model.ActivityLevel
import waterly.drinkwater.reminder.core.domain.model.VolumeUnit
import waterly.drinkwater.reminder.core.domain.model.WeightUnit
import waterly.drinkwater.reminder.core.presentation.UiState

data class SettingsState(
    val userName: String = "",
    val dailyGoal: Int = 2500,
    val weight: Float = 75f,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val volumeUnit: VolumeUnit = VolumeUnit.ML,
    val appVersion: String = "v1.0.0",
    val isLoading: Boolean = false
) : UiState