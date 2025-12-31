package com.drinkwater.reminder.core.presentation

/**
 * Base interface for all UI effects (one-shot events) in the application.
 * Effects are consumed once and don't persist in state.
 *
 * Use effects for navigation, showing toasts, or any action that should
 * happen only once and not survive configuration changes.
 *
 * Effects flow from ViewModel -> UI through a Channel (not StateFlow).
 *
 * Example:
 * ```
 * sealed interface HomeUiEffect : UiEffect {
 *     data object NavigateToSettings : HomeUiEffect
 *     data class ShowToast(val message: String) : HomeUiEffect
 *     data class ShowError(val error: String) : HomeUiEffect
 * }
 * ```
 */
interface UiEffect
