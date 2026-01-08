package com.drinkwater.reminder.features.progress.presentation

import com.drinkwater.reminder.core.presentation.UiEffect

/**
 * Side effects for the Progress screen
 */
sealed interface ProgressSideEffect : UiEffect {
    data object NavigateToHome : ProgressSideEffect
    data object NavigateToSettings : ProgressSideEffect
    data class ShowError(val message: String) : ProgressSideEffect
}
