package waterly.drinkwater.reminder.features.settings.presentation.weight

import waterly.drinkwater.reminder.core.presentation.UiEffect

sealed interface UpdateWeightEffect : UiEffect {
    data object NavigateBack : UpdateWeightEffect
    data class ShowError(val message: String) : UpdateWeightEffect
}
