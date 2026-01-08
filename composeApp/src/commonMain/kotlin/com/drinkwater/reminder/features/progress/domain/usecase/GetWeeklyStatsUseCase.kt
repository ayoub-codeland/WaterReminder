package com.drinkwater.reminder.features.progress.domain.usecase

import com.drinkwater.reminder.core.domain.model.WeeklyStats
import com.drinkwater.reminder.core.domain.repository.WaterIntakeRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

/**
 * Use case for getting weekly statistics
 */
class GetWeeklyStatsUseCase(
    private val repository: WaterIntakeRepository
) {
    suspend operator fun invoke(): WeeklyStats {
        val (startDate, endDate) = getCurrentWeekRange()
        return repository.getWeeklyStats(startDate, endDate)
    }
    
    private fun getCurrentWeekRange(): Pair<String, String> {
        val now = Clock.System.now()

        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        
        // Find Monday of current week
        val daysFromMonday = (today.dayOfWeek.ordinal) // Monday = 0
        val monday = today.minus(daysFromMonday, DateTimeUnit.DAY)
        val sunday = monday.plus(6, DateTimeUnit.DAY)
        
        return formatDate(monday) to formatDate(sunday)
    }
    
    private fun LocalDate.plus(days: Int, unit: DateTimeUnit.DayBased): LocalDate {
        return LocalDate.fromEpochDays(this.toEpochDays() + days)
    }
    
    private fun formatDate(date: LocalDate): String {
        val year = date.year
        val month = date.month.number.toString().padStart(2, '0')
        val day = date.dayOfMonth.toString().padStart(2, '0')
        return "$year-$month-$day"
    }
}
