package com.drinkwater.reminder.features.settings.presentation.profile

import com.drinkwater.reminder.core.domain.model.AgeGroup
import com.drinkwater.reminder.core.domain.model.BiologicalSex
import com.drinkwater.reminder.core.presentation.UiEvent

sealed interface EditProfileEvent : UiEvent {
    data class OnNameChanged(val name: String) : EditProfileEvent
    data class OnBiologicalSexChanged(val sex: BiologicalSex) : EditProfileEvent
    data class OnAgeGroupChanged(val ageGroup: AgeGroup) : EditProfileEvent
    data object OnProfileImageClick : EditProfileEvent
    data object OnSaveClick : EditProfileEvent
    data object OnCancelClick : EditProfileEvent
}
