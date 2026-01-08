package com.drinkwater.reminder.features.onboarding.presentation.welcome

import com.drinkwater.reminder.core.presentation.BaseViewModel

/**
 * ViewModel for Welcome Screen
 * 
 * Handles the initial welcome screen logic.
 * Minimal state - just navigation trigger.
 */
class WelcomeViewModel : BaseViewModel<WelcomeUiState, WelcomeUiEvent, WelcomeUiEffect>(
    initialState = WelcomeUiState()
) {
    override fun onEvent(event: WelcomeUiEvent) {
        when (event) {
            is WelcomeUiEvent.OnGetStartedClick -> handleGetStartedClick()
        }
    }
    
    private fun handleGetStartedClick() {
        sendEffect(WelcomeUiEffect.NavigateToProfileSetup)
    }
}
