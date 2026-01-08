package com.drinkwater.reminder.features.settings.domain.usecase

import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository

/**
 * Use case for saving/updating user profile
 */
class SaveUserProfileUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.saveProfile(profile)
    }
}
