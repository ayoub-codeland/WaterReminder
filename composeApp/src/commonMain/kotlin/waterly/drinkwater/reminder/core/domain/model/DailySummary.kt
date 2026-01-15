package waterly.drinkwater.reminder.core.domain.model

/**
 * Domain model representing daily water intake summary
 */
data class DailySummary(
    val date: String,
    val totalMl: Int,
    val goalMl: Int,
    val goalReached: Boolean,
    val lastUpdated: Long
) {
    val progress: Float
        get() = if (goalMl > 0) (totalMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f) else 0f
    
    val progressPercent: Int
        get() = (progress * 100).toInt()
    
    val remainingMl: Int
        get() = (goalMl - totalMl).coerceAtLeast(0)
}
