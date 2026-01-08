package com.drinkwater.reminder.features.progress.navigation

import com.drinkwater.reminder.core.navigation.NavigationDestination

/**
 * Navigation destinations for the Progress feature
 */
sealed interface ProgressDestination : NavigationDestination {
    data object Main : ProgressDestination {
        override val route = "progress"
    }
}
