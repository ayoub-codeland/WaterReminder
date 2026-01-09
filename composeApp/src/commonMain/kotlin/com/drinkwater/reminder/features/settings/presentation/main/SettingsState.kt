package com.drinkwater.reminder.features.settings.presentation.main

import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.VolumeUnit
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.presentation.UiState

data class SettingsState(
    val userName: String = "",
    val dailyGoal: Int = 2500,
    val weight: Float = 75f,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val volumeUnit: VolumeUnit = VolumeUnit.ML,
    val startOfWeek: String = "Monday",
    val appVersion: String = "v1.0.2",
    val isLoading: Boolean = false
) : UiState