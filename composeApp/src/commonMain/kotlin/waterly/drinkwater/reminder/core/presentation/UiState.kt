package waterly.drinkwater.reminder.core.presentation

/**
 * Base interface for all UI states in the application.
 * Enforces type-safe state management across all screens.
 *
 * Each screen should define its own data class implementing this interface
 * with all necessary state properties marked as val (immutable).
 *
 * Example:
 * ```
 * data class HomeUiState(
 *     val isLoading: Boolean = false,
 *     val userName: String = "",
 *     val errorMessage: String? = null
 * ) : UiState
 * ```
 */
interface UiState
