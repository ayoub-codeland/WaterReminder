package com.drinkwater.reminder.core.domain.model

/**
 * User profile domain model
 * 
 * Represents the core user data needed for calculating daily hydration goals
 * This is the single source of truth for user information
 */
data class UserProfile(
    val biologicalSex: BiologicalSex,
    val ageGroup: AgeGroup,
    val weight: Float,
    val weightUnit: WeightUnit,
    val activityLevel: ActivityLevel
) {
    /**
     * Get weight in kilograms (normalized)
     */
    val weightInKg: Float
        get() = weightUnit.toKg(weight)
    
    /**
     * Get weight in pounds (normalized)
     */
    val weightInLbs: Float
        get() = weightUnit.toLbs(weight)
}
