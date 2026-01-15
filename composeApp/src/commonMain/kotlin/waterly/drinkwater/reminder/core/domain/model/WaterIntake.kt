package waterly.drinkwater.reminder.core.domain.model

/**
 * Domain model representing a single water intake entry
 */
data class WaterIntake(
    val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long,
    val date: String
)
