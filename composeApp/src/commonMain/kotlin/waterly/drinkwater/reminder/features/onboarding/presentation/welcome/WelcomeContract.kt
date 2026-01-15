package waterly.drinkwater.reminder.features.onboarding.presentation.welcome

import waterly.drinkwater.reminder.core.presentation.UiEffect
import waterly.drinkwater.reminder.core.presentation.UiEvent
import waterly.drinkwater.reminder.core.presentation.UiState

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
