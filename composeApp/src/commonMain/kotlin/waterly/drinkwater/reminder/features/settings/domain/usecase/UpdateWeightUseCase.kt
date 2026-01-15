package waterly.drinkwater.reminder.features.settings.domain.usecase

import waterly.drinkwater.reminder.core.domain.model.WeightUnit
import waterly.drinkwater.reminder.core.domain.repository.UserProfileRepository
import waterly.drinkwater.reminder.core.domain.usecase.CalculateDailyGoalUseCase

/**
 * Use case for updating user's weight
 *
 * Encapsulates the business logic of updating weight in the user profile
 */
class UpdateWeightUseCase(
    private val repository: UserProfileRepository,
    private val calculateDailyGoal: CalculateDailyGoalUseCase
) {
    /**
     * Update user's weight and recalculate daily goal
     *
     * @param weight New weight value
     * @param unit Weight unit (KG or LBS)
     * @return New calculated daily goal in ml
     */
    suspend operator fun invoke(weight: Float, unit: WeightUnit): Int {
        // Update weight in repository
        repository.updateWeight(weight, unit)

        // Get updated profile
        val profile = repository.getProfile()
            ?: throw IllegalStateException("Profile must exist to update weight")

        // Recalculate daily goal with new weight
        val newGoal = calculateDailyGoal(profile)

        // Save new goal
        repository.saveDailyGoal(newGoal)

        return newGoal
    }
}