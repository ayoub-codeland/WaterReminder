package com.drinkwater.reminder.features.home.domain.usecase

import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting today's water intake total
 */
class GetTodayIntakeUseCase(
    private val repository: WaterIntakeRepository
) {
    fun asFlow(): Flow<Int> = repository.getTodayTotalFlow()
    
    suspend operator fun invoke(): Int = repository.getTodayTotal()
}
