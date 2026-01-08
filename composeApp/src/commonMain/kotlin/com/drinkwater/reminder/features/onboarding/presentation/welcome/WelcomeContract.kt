package com.drinkwater.reminder.features.onboarding.presentation.welcome

import com.drinkwater.reminder.core.presentation.UiEffect
import com.drinkwater.reminder.core.presentation.UiEvent
import com.drinkwater.reminder.core.presentation.UiState

/**
 * UI State for Welcome Screen
 */
data class WelcomeUiState(
    val isLoading: Boolean = false
) : UiState

/**
 * UI Events for Welcome Screen
 */
sealed interface WelcomeUiEvent : UiEvent {
    data object OnGetStartedClick : WelcomeUiEvent
}

/**
 * UI Effects for Welcome Screen
 */
sealed interface WelcomeUiEffect : UiEffect {
    data object NavigateToProfileSetup : WelcomeUiEffect
}
