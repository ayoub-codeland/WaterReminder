package waterly.drinkwater.reminder.features.main

sealed interface AppUiState {
    data object Loading : AppUiState
    data class Ready(val startDestination: String) : AppUiState
}