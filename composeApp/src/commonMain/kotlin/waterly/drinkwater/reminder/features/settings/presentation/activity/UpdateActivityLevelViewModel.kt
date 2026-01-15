package waterly.drinkwater.reminder.features.settings.presentation.activity

import androidx.lifecycle.viewModelScope
import waterly.drinkwater.reminder.core.domain.model.ActivityLevel
import waterly.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import waterly.drinkwater.reminder.core.presentation.BaseViewModel
import waterly.drinkwater.reminder.features.settings.domain.usecase.UpdateActivityLevelUseCase
import kotlinx.coroutines.launch

class UpdateActivityLevelViewModel(
    private val getUserProfile: GetUserProfileUseCase,
    private val updateActivityLevel: UpdateActivityLevelUseCase
) : BaseViewModel<UpdateActivityLevelState, UpdateActivityLevelEvent, UpdateActivityLevelEffect>(
    initialState = UpdateActivityLevelState(isLoading = true)
) {

    private var isSavingInProgress = false

    init {
        loadCurrentActivityLevel()
    }

    override fun onEvent(event: UpdateActivityLevelEvent) {
        when (event) {
            is UpdateActivityLevelEvent.OnActivityLevelChanged -> {
                updateState { copy(activityLevel = event.level) }
            }
            is UpdateActivityLevelEvent.OnSaveClick -> {
                saveActivityLevel()
            }
            is UpdateActivityLevelEvent.OnCancelClick -> {
                sendEffect(UpdateActivityLevelEffect.NavigateBack)
            }
            is UpdateActivityLevelEvent.OnBackClick -> {
                sendEffect(UpdateActivityLevelEffect.NavigateBack)
            }
        }
    }

    private fun loadCurrentActivityLevel() {
        viewModelScope.launch {
            try {
                val profile = getUserProfile()
                updateState {
                    copy(
                        activityLevel = profile?.activityLevel ?: ActivityLevel.MODERATE,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(UpdateActivityLevelEffect.ShowError("Failed to load data"))
            }
        }
    }

    private fun saveActivityLevel() {
        if (isSavingInProgress) return
        isSavingInProgress = true
        
        updateState { copy(isSaving = true) }

        viewModelScope.launch {
            try {
                updateActivityLevel(currentState.activityLevel)
                updateState { copy(isSaving = false) }
                sendEffect(UpdateActivityLevelEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isSaving = false) }
                isSavingInProgress = false
                sendEffect(UpdateActivityLevelEffect.ShowError(e.message ?: "Failed to save"))
            }
        }
    }
}
