package com.drinkwater.reminder.features.progress.presentation

import com.drinkwater.reminder.core.presentation.UiEvent

/**
 * Events for the Progress screen
 *
 * Note: Navigation between tabs is handled at app level (App.kt), not via events.
 * BackHandler in ProgressScreen directly calls onNavigateToHome callback.
 */
sealed interface ProgressEvent : UiEvent {
    data object OnRefresh : ProgressEvent
}
