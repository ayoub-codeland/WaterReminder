package com.drinkwater.reminder.features.progress.presentation

import com.drinkwater.reminder.core.presentation.UiEvent

/**
 * Events for the Progress screen
 */
sealed interface ProgressEvent : UiEvent {
    data object OnRefresh : ProgressEvent
    data object OnNavigateToHome : ProgressEvent
    data object OnNavigateToSettings : ProgressEvent
}
