package com.drinkwater.reminder.core.domain.repository

import com.drinkwater.reminder.core.domain.model.DailySummary
import com.drinkwater.reminder.core.domain.model.WaterIntake
import com.drinkwater.reminder.core.domain.model.WeeklyStats
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for water intake data
 * Following Clean Architecture and Dependency Inversion Principle
 */
interface WaterIntakeRepository {
    
    /**
     * Add a water intake record
     */
    suspend fun addWaterIntake(amountMl: Int): WaterIntake
    
    /**
     * Get today's total water intake as a Flow
     */
    fun getTodayTotalFlow(): Flow<Int>
    
    /**
     * Get today's total water intake once
     */
    suspend fun getTodayTotal(): Int
    
    /**
     * Get today's daily summary as a Flow
     */
    fun getTodaySummaryFlow(): Flow<DailySummary?>
    
    /**
     * Get daily summary for a specific date
     */
    suspend fun getDailySummary(date: String): DailySummary?
    
    /**
     * Get daily summaries for a date range
     */
    suspend fun getDailySummaries(startDate: String, endDate: String): List<DailySummary>
    
    /**
     * Get daily summaries as Flow for a date range
     */
    fun getDailySummariesFlow(startDate: String, endDate: String): Flow<List<DailySummary>>
    
    /**
     * Get weekly statistics
     */
    suspend fun getWeeklyStats(weekStartDate: String, weekEndDate: String): WeeklyStats
    
    /**
     * Get current streak (consecutive days with goal reached)
     */
    suspend fun getCurrentStreak(): Int
    
    /**
     * Reset today's water intake
     */
    suspend fun resetTodayIntake()
    
    /**
     * Delete all water intake data
     */
    suspend fun deleteAllData()
}
