package com.drinkwater.reminder.core.domain.model

/**
 * Domain model representing weekly statistics
 */
data class WeeklyStats(
    val weekStartDate: String,
    val weekEndDate: String,
    val totalMl: Int,
    val dailyAverageMl: Int,
    val daysTracked: Int,
    val goalsReached: Int,
    val currentStreak: Int,
    val bestDay: DailySummary?,
    val dailySummaries: List<DailySummary>,
    val percentageChangeFromLastWeek: Int? = null
) {
    val totalLiters: Float
        get() = totalMl / 1000f
    
    val dailyAverageLiters: Float
        get() = dailyAverageMl / 1000f
    
    val completionRate: Int
        get() = if (daysTracked > 0) ((goalsReached.toFloat() / daysTracked) * 100).toInt() else 0
}
