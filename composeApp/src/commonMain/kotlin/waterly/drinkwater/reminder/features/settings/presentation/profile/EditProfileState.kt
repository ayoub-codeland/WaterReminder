package waterly.drinkwater.reminder.features.settings.presentation.profile

import waterly.drinkwater.reminder.core.domain.model.AgeGroup
import waterly.drinkwater.reminder.core.domain.model.BiologicalSex
import waterly.drinkwater.reminder.core.presentation.UiState

data class EditProfileState(
    val displayName: String = "",
    val biologicalSex: BiologicalSex = BiologicalSex.MALE,
    val ageGroup: AgeGroup = AgeGroup.AGE_18_30,
    val profileImageUrl: String? = null,
    val isSaving: Boolean = false,
    val isLoading: Boolean = false
) : UiState

// Helper to get display label for AgeGroup
fun AgeGroup.toDisplayLabel(): String = when (this) {
    AgeGroup.AGE_18_30 -> "18 - 30"
    AgeGroup.AGE_31_50 -> "31 - 50"
    AgeGroup.AGE_51_60 -> "51 - 60"
    AgeGroup.AGE_60_PLUS -> "60+"
}
