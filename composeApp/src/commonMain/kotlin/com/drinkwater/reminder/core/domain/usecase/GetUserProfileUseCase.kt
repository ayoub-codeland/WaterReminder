package com.drinkwater.reminder.core.domain.usecase

import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository

/**
 * Use case for getting user profile
 * 
 * Encapsulates the business logic of retrieving user profile data
 */
class GetUserProfileUseCase(
    private val repository: UserProfileRepository
) {
    /**
     * Get user profile from persistent storage
     * 
     * @return User profile if exists, null otherwise
     */
    suspend operator fun invoke(): UserProfile? {
        return repository.getProfile()
    }
}
