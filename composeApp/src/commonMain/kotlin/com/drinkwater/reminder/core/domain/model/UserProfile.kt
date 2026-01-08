package com.drinkwater.reminder.core.domain.model

import kotlinx.serialization.Serializable

/**
 * User profile domain model
 * 
 * Represents the core user data needed for calculating daily hydration goals
 * This is the single source of truth for user information
 */
@Serializable
data class UserProfile(
    val username: String? = null,
    val biologicalSex: BiologicalSex = BiologicalSex.MALE,
    val ageGroup: AgeGroup = AgeGroup.AGE_18_30,
    val weight: Float = 75.0f,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val dailyGoal: Int = 2500,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
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
