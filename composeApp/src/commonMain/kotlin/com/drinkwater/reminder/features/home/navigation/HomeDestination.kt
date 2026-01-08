package com.drinkwater.reminder.features.home.navigation

import com.drinkwater.reminder.core.navigation.NavigationDestination

/**
 * Navigation destinations for the Home feature
 */
sealed interface HomeDestination : NavigationDestination {
    data object Main : HomeDestination {
        override val route = "home"
    }
}
