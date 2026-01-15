package waterly.drinkwater.reminder.core.domain.model

import kotlinx.serialization.Serializable

/**
 * Biological sex for calculating hydration needs
 * 
 * Domain model - represents core business concept
 * Used for calculating daily water goal multipliers
 */
@Serializable
enum class BiologicalSex(val multiplier: Float) {
    MALE(1.0f),
    FEMALE(0.88f)
}
