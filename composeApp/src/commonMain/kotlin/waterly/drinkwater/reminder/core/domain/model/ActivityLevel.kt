package waterly.drinkwater.reminder.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Activity level for calculating hydration needs
 * 
 * Domain model - represents core business concept
 * Higher activity levels require more water intake
 */
@Serializable
enum class ActivityLevel(
    val multiplier: Float,
    val displayName: String,
    val description: String,
    val additionalWaterMl: Int
) {
    SEDENTARY(
        multiplier = 1.0f,
        displayName = "Sedentary",
        description = "Little to no exercise",
        additionalWaterMl = 0
    ),
    LIGHT(
        multiplier = 1.1f,
        displayName = "Lightly Active",
        description = "You exercise 1-2 times a week at a light intensity.",
        additionalWaterMl = 250
    ),
    MODERATE(
        multiplier = 1.2f,
        displayName = "Moderately Active",
        description = "You exercise 3-5 times a week at a medium intensity.",
        additionalWaterMl = 500
    ),
    ACTIVE(
        multiplier = 1.3f,
        displayName = "Very Active",
        description = "You exercise 5-6 times a week at a high intensity.",
        additionalWaterMl = 750
    ),
    VERY_ACTIVE(
        multiplier = 1.4f,
        displayName = "Extremely Active",
        description = "You exercise daily at a very high intensity or physical job.",
        additionalWaterMl = 1000
    );
    
    /**
     * Convert to slider value (1-5)
     */
    fun toSliderValue(): Float = when (this) {
        SEDENTARY -> 1f
        LIGHT -> 2f
        MODERATE -> 3f
        ACTIVE -> 4f
        VERY_ACTIVE -> 5f
    }
    
    companion object {
        /**
         * Create from slider value (1-5)
         */
        fun fromSliderValue(value: Float): ActivityLevel = when (value.toInt()) {
            1 -> SEDENTARY
            2 -> LIGHT
            3 -> MODERATE
            4 -> ACTIVE
            else -> VERY_ACTIVE
        }
    }
}
