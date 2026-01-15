package waterly.drinkwater.reminder.features.settings.presentation.activity

import waterly.drinkwater.reminder.core.domain.model.ActivityLevel
import waterly.drinkwater.reminder.core.presentation.UiEvent

sealed interface UpdateActivityLevelEvent : UiEvent {
    data class OnActivityLevelChanged(val level: ActivityLevel) : UpdateActivityLevelEvent
    data object OnSaveClick : UpdateActivityLevelEvent
    data object OnCancelClick : UpdateActivityLevelEvent
    data object OnBackClick : UpdateActivityLevelEvent
}
