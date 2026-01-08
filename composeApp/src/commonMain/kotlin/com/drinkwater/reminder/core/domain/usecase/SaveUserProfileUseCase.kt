package com.drinkwater.reminder.core.domain.usecase

import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository

/**
 * Use case for saving user profile
 * 
 * Encapsulates the business logic of persisting user profile data
 */
class SaveUserProfileUseCase(
    private val repository: UserProfileRepository
) {
    /**
     * Save user profile to persistent storage
     * 
     * @param profile User profile to save
     */
    suspend operator fun invoke(profile: UserProfile) {
        repository.saveProfile(profile)
    }
}
