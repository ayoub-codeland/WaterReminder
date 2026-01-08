package com.drinkwater.reminder.features.settings.domain.usecase

import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase

/**
 * Use case for updating user's activity level
 *
 * Encapsulates the business logic of updating activity level in the user profile
 */
class UpdateActivityLevelUseCase(
    private val repository: UserProfileRepository,
    private val calculateDailyGoal: CalculateDailyGoalUseCase
) {
    /**
     * Update user's activity level and recalculate daily goal
     *
     * @param level New activity level
     * @return New calculated daily goal in ml
     */
    suspend operator fun invoke(level: ActivityLevel): Int {
        // Update activity level in repository
        repository.updateActivityLevel(level)

        // Get updated profile
        val profile = repository.getProfile()
            ?: throw IllegalStateException("Profile must exist to update activity level")

        // Recalculate daily goal with new activity level
        val newGoal = calculateDailyGoal(profile)

        // Save new goal
        repository.saveDailyGoal(newGoal)

        return newGoal
    }
}