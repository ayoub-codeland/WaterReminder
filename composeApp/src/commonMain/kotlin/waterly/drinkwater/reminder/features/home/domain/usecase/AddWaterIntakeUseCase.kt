package waterly.drinkwater.reminder.features.home.domain.usecase

import waterly.drinkwater.reminder.core.domain.model.WaterIntake
import waterly.drinkwater.reminder.core.domain.repository.WaterIntakeRepository

/**
 * Use case for adding water intake
 * Single Responsibility: Only handles adding water
 */
class AddWaterIntakeUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(amountMl: Int): WaterIntake {
        require(amountMl > 0) { "Amount must be positive" }
        require(amountMl <= 5000) { "Amount cannot exceed 5000ml" }
        return repository.addWaterIntake(amountMl)
    }
}
