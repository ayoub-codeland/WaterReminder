package com.drinkwater.reminder.features.settings.presentation.profile

import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getUserProfile: GetUserProfileUseCase
) : BaseViewModel<EditProfileState, EditProfileEvent, EditProfileEffect>(
    initialState = EditProfileState(isLoading = true)
) {

    init {
        loadProfile()
    }

    override fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.OnNameChanged -> {
                updateState { copy(displayName = event.name) }
            }
            is EditProfileEvent.OnBiologicalSexChanged -> {
                updateState { copy(biologicalSex = event.sex) }
            }
            is EditProfileEvent.OnAgeGroupChanged -> {
                updateState { copy(ageGroup = event.ageGroup) }
            }
            is EditProfileEvent.OnProfileImageClick -> {
                sendEffect(EditProfileEffect.OpenImagePicker)
            }
            is EditProfileEvent.OnSaveClick -> {
                saveProfile()
            }
            is EditProfileEvent.OnCancelClick -> {
                sendEffect(EditProfileEffect.NavigateBack)
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = getUserProfile()
                updateState {
                    copy(
                        biologicalSex = profile?.biologicalSex ?: currentState.biologicalSex,
                        ageGroup = profile?.ageGroup ?: currentState.ageGroup,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(EditProfileEffect.ShowError("Failed to load profile"))
            }
        }
    }

    private fun saveProfile() {
        updateState { copy(isSaving = true) }
        
        viewModelScope.launch {
            try {
                // TODO: Save profile with repository
                updateState { copy(isSaving = false) }
                sendEffect(EditProfileEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isSaving = false) }
                sendEffect(EditProfileEffect.ShowError(e.message ?: "Failed to save"))
            }
        }
    }
}
