package waterly.drinkwater.reminder.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Age group for calculating hydration needs
 * 
 * Domain model - represents core business concept
 * Older age groups typically require slightly less water
 */
@Serializable
enum class AgeGroup(val multiplier: Float) {
    AGE_18_30(1.0f),
    AGE_31_50(0.95f),
    AGE_51_60(0.9f),
    AGE_60_PLUS(0.85f)
}
