package waterly.drinkwater.reminder.features.settings.presentation.activity

import waterly.drinkwater.reminder.core.presentation.UiEffect

sealed interface UpdateActivityLevelEffect : UiEffect {
    data object NavigateBack : UpdateActivityLevelEffect
    data class ShowError(val message: String) : UpdateActivityLevelEffect
}
