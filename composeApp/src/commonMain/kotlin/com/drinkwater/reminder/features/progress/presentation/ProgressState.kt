package com.drinkwater.reminder.features.progress.presentation

import com.drinkwater.reminder.core.domain.model.DailySummary
import com.drinkwater.reminder.core.domain.model.WeeklyStats
import com.drinkwater.reminder.core.presentation.UiState
import com.drinkwater.reminder.features.progress.domain.usecase.AllTimeStats

/**
 * Immutable state for the Progress screen
 */
data class ProgressState(
    // Weekly data
    val weeklyStats: WeeklyStats? = null,
    val weeklyTotal: String = "0L",
    val percentageChange: Int? = null,
    val currentStreak: Int = 0,
    
    // Stats cards
    val dailyAverage: String = "0L",
    val dailyAverageChange: String? = null,
    val allTimeTotal: String = "0L",
    val allTimeSince: String = "",
    val bestDay: String = "—",
    val bestDayAmount: String = "0 Liters",
    val completionRate: Int = 0,
    val daysMetGoal: String = "0/0 Days met",
    
    // Chart data - 7 days (Mon-Sun)
    val chartData: List<DayChartData> = emptyList(),
    
    // Smart tip
    val smartTip: String = "Start tracking to see personalized tips!",
    
    // Loading
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

/**
 * Data for a single day in the weekly chart
 */
data class DayChartData(
    val dayLabel: String, // "M", "T", "W", etc.
    val dayName: String,  // Full name for accessibility
    val totalMl: Int,
    val goalMl: Int,
    val isToday: Boolean = false,
    val isFuture: Boolean = false
) {
    val progress: Float
        get() = if (goalMl > 0) (totalMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1.2f) else 0f
    
    val goalReached: Boolean
        get() = totalMl >= goalMl
}
