package com.drinkwater.reminder.features.dashboard.presentation

import com.drinkwater.reminder.core.presentation.UiEvent

sealed interface DashboardUiEvent : UiEvent {
    data class OnAddWater(val amount: Int) : DashboardUiEvent
}