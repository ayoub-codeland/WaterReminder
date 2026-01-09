package com.drinkwater.reminder.features.progress.presentation

import com.drinkwater.reminder.core.presentation.UiEffect

/**
 * Side effects for the Progress screen
 *
 * Note: Navigation between tabs is handled at app level (App.kt), not via side effects.
 * BackHandler in ProgressScreen directly calls onNavigateToHome callback.
 * This keeps the feature decoupled and follows Clean Architecture principles.
 */
sealed interface ProgressSideEffect : UiEffect {
    data class ShowError(val message: String) : ProgressSideEffect
}
