package com.drinkwater.reminder.features.home.presentation

import com.drinkwater.reminder.core.presentation.UiEffect

/**
 * Side effects for the Home (Dashboard) screen
 * One-shot events that don't persist in state
 *
 * Note: Navigation between tabs is handled at app level (App.kt), not via side effects.
 * This keeps the feature decoupled and follows Clean Architecture principles.
 */
sealed interface HomeSideEffect : UiEffect {
    // Notifications/Feedback
    data object ShowGoalReachedCelebration : HomeSideEffect
    data class ShowError(val message: String) : HomeSideEffect
    data class ShowWaterAddedFeedback(val amount: Int) : HomeSideEffect
}
