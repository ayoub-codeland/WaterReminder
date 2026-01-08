package com.drinkwater.reminder.core.domain.usecase

import com.drinkwater.reminder.core.domain.model.UserProfile

/**
 * Use case for calculating daily water intake goal
 * 
 * Business logic for determining how much water a user should drink daily
 * based on their profile characteristics
 * 
 * Formula:
 * Base water = weight (kg) * 35 ml
 * Final goal = base * activity multiplier * age multiplier * sex multiplier
 */
class CalculateDailyGoalUseCase {
    
    /**
     * Calculate daily water goal in milliliters
     * 
     * @param profile User profile with all necessary information
     * @return Daily water goal in milliliters
     */
    operator fun invoke(profile: UserProfile): Int {
        // Convert weight to kg if needed
        val weightInKg = profile.weightInKg
        
        // Base calculation: 35ml per kg of body weight
        val baseWaterMl = weightInKg * BASE_ML_PER_KG
        
        // Apply multipliers based on user characteristics
        val totalMultiplier = profile.activityLevel.multiplier *
                              profile.ageGroup.multiplier *
                              profile.biologicalSex.multiplier
        
        // Calculate final goal
        val dailyGoalMl = baseWaterMl * totalMultiplier
        
        // Round to nearest 50ml for cleaner numbers
        return (dailyGoalMl / 50).toInt() * 50
    }
    
    companion object {
        /**
         * Base water requirement per kilogram of body weight
         */
        private const val BASE_ML_PER_KG = 35f
    }
}
