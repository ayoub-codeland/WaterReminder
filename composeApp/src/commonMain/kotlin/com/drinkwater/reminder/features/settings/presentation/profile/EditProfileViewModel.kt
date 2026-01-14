package com.drinkwater.reminder.features.settings.presentation.profile

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import com.drinkwater.reminder.core.domain.usecase.SaveUserProfileUseCase
import com.drinkwater.reminder.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val saveUserProfile: SaveUserProfileUseCase
) : BaseViewModel<EditProfileState, EditProfileEvent, EditProfileEffect>(
    initialState = EditProfileState(isLoading = true)
) {

    private var isSavingInProgress = false

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
                if (profile != null) {
                    updateState {
                        copy(
                            displayName = profile.username ?: "",
                            biologicalSex = profile.biologicalSex,
                            ageGroup = profile.ageGroup,
                            isLoading = false
                        )
                    }
                } else {
                    updateState { copy(isLoading = false) }
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(EditProfileEffect.ShowError("Failed to load profile"))
            }
        }
    }

    private fun saveProfile() {
        if (isSavingInProgress) return
        isSavingInProgress = true
        
        updateState { copy(isSaving = true) }
        
        viewModelScope.launch {
            try {
                val currentProfile = getUserProfile()
                
                val updatedProfile = UserProfile(
                    username = currentState.displayName.takeIf { it.isNotBlank() },
                    biologicalSex = currentState.biologicalSex,
                    ageGroup = currentState.ageGroup,
                    weight = currentProfile?.weight ?: 75.0f,
                    weightUnit = currentProfile?.weightUnit ?: com.drinkwater.reminder.core.domain.model.WeightUnit.KG,
                    activityLevel = currentProfile?.activityLevel ?: com.drinkwater.reminder.core.domain.model.ActivityLevel.MODERATE,
                    dailyGoal = currentProfile?.dailyGoal ?: 2500,
                    createdAt = currentProfile?.createdAt ?: System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                
                saveUserProfile(updatedProfile)
                updateState { copy(isSaving = false) }
                sendEffect(EditProfileEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isSaving = false) }
                isSavingInProgress = false
                sendEffect(EditProfileEffect.ShowError(e.message ?: "Failed to save"))
            }
        }
    }
}
