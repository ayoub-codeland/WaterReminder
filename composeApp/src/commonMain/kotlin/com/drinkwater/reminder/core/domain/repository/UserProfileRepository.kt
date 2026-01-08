package com.drinkwater.reminder.core.domain.repository

import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.UserProfile
import com.drinkwater.reminder.core.domain.model.VolumeUnit
import com.drinkwater.reminder.core.domain.model.WeightUnit

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user profile data
 * 
 * This is a domain layer abstraction - the actual implementation is in the data layer
 * Following the Dependency Inversion Principle (SOLID)
 */
interface UserProfileRepository {
    /**
     * Save complete user profile
     */
    suspend fun saveProfile(profile: UserProfile)
    
    /**
     * Get current user profile
     * Returns null if no profile exists yet
     */
    suspend fun getProfile(): UserProfile?
    
    /**
     * Observe user profile changes as a Flow
     * Returns null if no profile exists yet
     */
    fun observeProfile(): Flow<UserProfile?>
    
    /**
     * Update user's weight
     */
    suspend fun updateWeight(weight: Float, unit: WeightUnit)
    
    /**
     * Update user's activity level
     */
    suspend fun updateActivityLevel(level: ActivityLevel)
    
    /**
     * Save daily water goal
     */
    suspend fun saveDailyGoal(goalMl: Int)
    
    /**
     * Get saved daily water goal
     */
    suspend fun getDailyGoal(): Int?
    
    /**
     * Save preferred volume unit (ml or oz)
     */
    suspend fun saveVolumeUnit(unit: VolumeUnit)
    
    /**
     * Get preferred volume unit
     */
    suspend fun getVolumeUnit(): VolumeUnit
}
