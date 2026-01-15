package waterly.drinkwater.reminder.core.presentation

/**
 * Base interface for all UI events in the application.
 * UI events represent user interactions and intents.
 *
 * Events flow from UI -> ViewModel and are processed to update state.
 * Each screen should define a sealed interface with specific event types.
 *
 * Example:
 * ```
 * sealed interface HomeUiEvent : UiEvent {
 *     data object OnRefreshClicked : HomeUiEvent
 *     data class OnItemSelected(val id: String) : HomeUiEvent
 *     data class OnInputChanged(val value: String) : HomeUiEvent
 * }
 * ```
 */
interface UiEvent
