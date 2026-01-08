package com.drinkwater.reminder.features.settings.presentation.profile

import com.drinkwater.reminder.core.presentation.UiEffect

sealed interface EditProfileEffect : UiEffect {
    data object NavigateBack : EditProfileEffect
    data class ShowError(val message: String) : EditProfileEffect
    data object OpenImagePicker : EditProfileEffect
}
