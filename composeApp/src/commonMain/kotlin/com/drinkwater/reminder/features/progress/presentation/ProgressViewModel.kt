package com.drinkwater.reminder.features.progress.presentation

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.model.DailySummary
import com.drinkwater.reminder.core.presentation.BaseViewModel
import com.drinkwater.reminder.features.home.domain.usecase.GetCurrentStreakUseCase
import com.drinkwater.reminder.features.progress.domain.usecase.AllTimeStats
import com.drinkwater.reminder.features.progress.domain.usecase.GetAllTimeStatsUseCase
import com.drinkwater.reminder.features.progress.domain.usecase.GetWeeklyStatsUseCase
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt

/**
 * ViewModel for Progress Screen
 */
class ProgressViewModel(
    private val getWeeklyStatsUseCase: GetWeeklyStatsUseCase,
    private val getAllTimeStatsUseCase: GetAllTimeStatsUseCase,
    private val getCurrentStreakUseCase: GetCurrentStreakUseCase
) : BaseViewModel<ProgressState, ProgressEvent, ProgressSideEffect>(
    initialState = ProgressState()
) {
    
    init {
        loadData()
    }
    
    override fun onEvent(event: ProgressEvent) {
        when (event) {
            ProgressEvent.OnRefresh -> loadData()
        }
    }
    
    private fun loadData() {
        viewModelScope.launch {
            try {
                updateState { copy(isLoading = true, error = null) }
                
                val weeklyStats = getWeeklyStatsUseCase()
                val allTimeStats = getAllTimeStatsUseCase()
                val streak = getCurrentStreakUseCase()
                
                val chartData = buildChartData(weeklyStats.dailySummaries, weeklyStats.weekStartDate)
                val smartTip = generateSmartTip(weeklyStats, allTimeStats)
                
                updateState {
                    copy(
                        weeklyStats = weeklyStats,
                        weeklyTotal = formatLiters(weeklyStats.totalMl),
                        percentageChange = weeklyStats.percentageChangeFromLastWeek,
                        currentStreak = streak,
                        dailyAverage = formatLiters(weeklyStats.dailyAverageMl),
                        dailyAverageChange = weeklyStats.percentageChangeFromLastWeek?.let { 
                            if (it >= 0) "+$it% vs last week" else "$it% vs last week" 
                        },
                        allTimeTotal = formatLiters(allTimeStats.totalMl),
                        allTimeSince = formatSinceDate(allTimeStats.startDate),
                        bestDay = weeklyStats.bestDay?.let { getDayName(it.date) } ?: "—",
                        bestDayAmount = weeklyStats.bestDay?.let { formatLiters(it.totalMl) } ?: "0 Liters",
                        completionRate = allTimeStats.completionRate,
                        daysMetGoal = "${allTimeStats.goalsReached}/${allTimeStats.daysTracked} Days met",
                        chartData = chartData,
                        smartTip = smartTip,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { 
                    copy(
                        isLoading = false, 
                        error = e.message ?: "Failed to load progress"
                    ) 
                }
                sendEffect(ProgressSideEffect.ShowError(e.message ?: "Failed to load progress"))
            }
        }
    }
    
    private fun buildChartData(summaries: List<DailySummary>, weekStartDate: String): List<DayChartData> {
        val now = Clock.System.now()
        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val todayStr = formatDate(today)

        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
        val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        // Parse week start date
        val parts = weekStartDate.split("-")
        val startDate = LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())

        // Build initial chart data
        val chartData = dayLabels.mapIndexed { index, label ->
            val date = LocalDate.fromEpochDays(startDate.toEpochDays() + index)
            val dateStr = formatDate(date)
            val summary = summaries.find { it.date == dateStr }
            val isFuture = date.toEpochDays() > today.toEpochDays()

            DayChartData(
                dayLabel = label,
                dayName = dayNames[index],
                totalMl = summary?.totalMl ?: 0,
                goalMl = summary?.goalMl ?: 2500,
                isToday = dateStr == todayStr,
                isFuture = isFuture,
                isBestDay = false,  // Will be set below
                isWorstDay = false  // Will be set below
            )
        }

        // Business logic: Identify best and worst days (only non-future days with water intake)
        val daysWithWater = chartData.filter { !it.isFuture && it.totalMl > 0 }
        val bestDayTotalMl = daysWithWater.maxOfOrNull { it.totalMl }
        val worstDayTotalMl = daysWithWater.minOfOrNull { it.totalMl }

        // Mark best/worst days in data
        return chartData.map { day ->
            day.copy(
                isBestDay = !day.isFuture && day.totalMl > 0 && day.totalMl == bestDayTotalMl,
                isWorstDay = !day.isFuture && day.totalMl > 0 && day.totalMl == worstDayTotalMl && bestDayTotalMl != worstDayTotalMl
            )
        }
    }
    
    private fun generateSmartTip(weeklyStats: com.drinkwater.reminder.core.domain.model.WeeklyStats, allTimeStats: AllTimeStats): String {
        val chartData = buildChartData(weeklyStats.dailySummaries, weeklyStats.weekStartDate)
        
        // Check for weekend pattern
        val weekdayAvg = chartData.take(5).map { it.totalMl }.average()
        val weekendAvg = chartData.takeLast(2).map { it.totalMl }.average()
        
        return when {
            weekendAvg < weekdayAvg * 0.7 -> 
                "You tend to drink less on weekends. Try setting a specific weekend reminder to keep your streak!"
            weeklyStats.currentStreak >= 7 -> 
                "Amazing! You've maintained a ${weeklyStats.currentStreak}-day streak. Keep it up! 🎉"
            allTimeStats.completionRate >= 90 -> 
                "Excellent! You're hitting your goals ${allTimeStats.completionRate}% of the time. You're a hydration champion!"
            allTimeStats.completionRate < 50 -> 
                "Try drinking a glass of water first thing in the morning to kickstart your hydration habit."
            else -> 
                "Consistency is key! Try to drink water at the same times each day to build a lasting habit."
        }
    }
    
    private fun formatLiters(ml: Int): String {
        val liters = ml / 1000f
        return if (liters >= 10) {
            "${liters.toInt()}L"
        } else {
            // Manual formatting to avoid String.format
            val rounded = (liters * 10).roundToInt() / 10f
            val intPart = rounded.toInt()
            val decPart = ((rounded - intPart) * 10).roundToInt()
            if (decPart == 0) "${intPart}L" else "$intPart.${decPart}L"
        }
    }
    
    private fun formatDate(date: LocalDate): String {
        val year = date.year
        val month = date.monthNumber.toString().padStart(2, '0')
        val day = date.dayOfMonth.toString().padStart(2, '0')
        return "$year-$month-$day"
    }
    
    private fun formatSinceDate(dateStr: String): String {
        val parts = dateStr.split("-")
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val monthIndex = parts[1].toInt() - 1
        val day = parts[2].toInt()
        return "Since ${months[monthIndex]} ${day}${getDaySuffix(day)}"
    }
    
    private fun getDaySuffix(day: Int): String {
        return when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }
    }
    
    private fun getDayName(dateStr: String): String {
        val parts = dateStr.split("-")
        val date = LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        return date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    }
}
