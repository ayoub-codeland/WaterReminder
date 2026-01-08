package com.drinkwater.reminder.features.home.presentation

import com.drinkwater.reminder.core.presentation.UiState

/**
 * Immutable state for the Home (Dashboard) screen
 * Follows UDF pattern - single source of truth for UI state
 */
data class HomeState(
    // Water tracking
    val currentIntake: Int = 0,
    val dailyGoal: Int = 2500,
    
    // User info
    val userName: String = "Alex",
    val currentStreak: Int = 12,
    
    // Date display
    val currentDate: String = "Monday, Oct 24",
    val greeting: String = "Good Morning",
    
    // Daily tip
    val dailyTip: String = "Drinking water now helps avoid the 3 PM energy slump.",
    val showDailyTip: Boolean = true,
    
    // Dialog state
    val showAddWaterDialog: Boolean = false,
    val customWaterAmount: String = "",
    val customAmountError: String? = null,
    
    // Loading state
    val isLoading: Boolean = false
) : UiState {
    
    /**
     * Calculated progress (0f to 1f)
     */
    val progress: Float
        get() = if (dailyGoal > 0) {
            (currentIntake.toFloat() / dailyGoal.toFloat()).coerceIn(0f, 1f)
        } else 0f
    
    /**
     * Progress as percentage for display (0 to 100)
     */
    val progressPercent: Int
        get() = (progress * 100).toInt()
    
    /**
     * Remaining water to reach goal
     */
    val remainingIntake: Int
        get() = (dailyGoal - currentIntake).coerceAtLeast(0)
    
    /**
     * Check if goal is completed
     */
    val isGoalCompleted: Boolean
        get() = currentIntake >= dailyGoal
}
