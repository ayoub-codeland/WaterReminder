package com.drinkwater.reminder.features.progress.domain.usecase

import com.drinkwater.reminder.core.domain.model.DailySummary
import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Use case for getting all-time statistics
 */
class GetAllTimeStatsUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(): AllTimeStats {
        // Get data from start of tracking (assume Oct 1st of current year for now)
        val now = Clock.System.now()
        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        val startDate = LocalDate(today.year, 10, 1) // Oct 1st
        val startDateStr = formatDate(startDate)
        val endDateStr = formatDate(today)
        
        val summaries = repository.getDailySummaries(startDateStr, endDateStr)
        
        val totalMl = summaries.sumOf { it.totalMl }
        val avgMl = if (summaries.isNotEmpty()) totalMl / summaries.size else 0
        val goalsReached = summaries.count { it.goalReached }
        val bestDay = summaries.maxByOrNull { it.totalMl }
        
        return AllTimeStats(
            totalMl = totalMl,
            dailyAverageMl = avgMl,
            daysTracked = summaries.size,
            goalsReached = goalsReached,
            bestDay = bestDay,
            startDate = startDateStr
        )
    }
    
    private fun formatDate(date: LocalDate): String {
        val year = date.year
        val month = date.monthNumber.toString().padStart(2, '0')
        val day = date.dayOfMonth.toString().padStart(2, '0')
        return "$year-$month-$day"
    }
}

data class AllTimeStats(
    val totalMl: Int,
    val dailyAverageMl: Int,
    val daysTracked: Int,
    val goalsReached: Int,
    val bestDay: DailySummary?,
    val startDate: String
) {
    val totalLiters: Float
        get() = totalMl / 1000f
    
    val dailyAverageLiters: Float
        get() = dailyAverageMl / 1000f
    
    val completionRate: Int
        get() = if (daysTracked > 0) ((goalsReached.toFloat() / daysTracked) * 100).toInt() else 0
}
