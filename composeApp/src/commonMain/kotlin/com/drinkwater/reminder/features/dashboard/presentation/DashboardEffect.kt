package com.drinkwater.reminder.features.dashboard.presentation

import com.drinkwater.reminder.core.presentation.UiEffect

sealed interface DashboardUiEffect : UiEffect {
    data object ShowGoalReached : DashboardUiEffect
}