package com.drinkwater.reminder.features.home.presentation

import com.drinkwater.reminder.core.presentation.UiEffect

/**
 * Side effects for the Home (Dashboard) screen
 * One-shot events that don't persist in state
 */
sealed interface HomeSideEffect : UiEffect {
    
    // Navigation
    data object NavigateToProgress : HomeSideEffect
    data object NavigateToSettings : HomeSideEffect
    
    // Notifications/Feedback
    data object ShowGoalReachedCelebration : HomeSideEffect
    data class ShowError(val message: String) : HomeSideEffect
    data class ShowWaterAddedFeedback(val amount: Int) : HomeSideEffect
}
