package com.drinkwater.reminder.features.home.domain.usecase

import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository

/**
 * Use case for getting current streak
 */
class GetCurrentStreakUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(): Int = repository.getCurrentStreak()
}
