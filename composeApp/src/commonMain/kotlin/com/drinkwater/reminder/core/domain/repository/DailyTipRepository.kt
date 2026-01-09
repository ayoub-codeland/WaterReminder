package com.drinkwater.reminder.core.domain.repository

import com.drinkwater.reminder.core.domain.model.DailyTip

/**
 * Repository interface for daily hydration tips
 * 
 * This is a domain layer abstraction following Dependency Inversion Principle
 */
interface DailyTipRepository {
    /**
     * Load all tips from local storage
     * Should be called once on app startup
     */
    suspend fun loadTips()
    
    /**
     * Get a random tip from the loaded tips
     * Returns a default tip if none are loaded
     */
    suspend fun getRandomTip(): DailyTip
}
