package com.drinkwater.reminder.core.domain.usecase

import com.drinkwater.reminder.core.domain.repository.DailyTipRepository

/**
 * Use case for getting a daily hydration tip
 * 
 * Encapsulates the business logic of retrieving tips
 * Returns a random tip each time for MVP
 */
class GetDailyTipUseCase(
    private val repository: DailyTipRepository
) {
    /**
     * Get a random hydration tip
     * 
     * @return Tip content string
     */
    suspend operator fun invoke(): String {
        return repository.getRandomTip().content
    }
}
