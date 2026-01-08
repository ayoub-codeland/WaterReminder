package com.drinkwater.reminder.features.home.domain.usecase

import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository

/**
 * Use case for resetting today's water intake
 */
class ResetTodayIntakeUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke() {
        repository.resetTodayIntake()
    }
}
