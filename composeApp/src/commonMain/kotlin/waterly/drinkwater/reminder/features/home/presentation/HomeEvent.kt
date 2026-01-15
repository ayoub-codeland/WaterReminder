package waterly.drinkwater.reminder.features.home.presentation

import waterly.drinkwater.reminder.core.presentation.UiEvent

/**
 * Events for the Home (Dashboard) screen
 * Represents all user interactions
 */
sealed interface HomeEvent : UiEvent {
    
    // Water tracking events
    data class OnAddWater(val amount: Int) : HomeEvent
    data object OnAddWaterClick : HomeEvent
    data object OnDismissAddWaterDialog : HomeEvent
    data class OnCustomAmountChanged(val amount: String) : HomeEvent
    data object OnConfirmCustomAmount : HomeEvent
    
    // Quick add presets
    data object OnAddGlass : HomeEvent      // 250ml
    data object OnAddBottle : HomeEvent     // 500ml
    
    // Daily tip
    data object OnDismissDailyTip : HomeEvent

    // Reset (for testing/debugging)
    data object OnResetIntake : HomeEvent
}
