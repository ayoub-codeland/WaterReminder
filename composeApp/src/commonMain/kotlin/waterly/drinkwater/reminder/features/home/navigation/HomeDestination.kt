package waterly.drinkwater.reminder.features.home.navigation

import waterly.drinkwater.reminder.core.navigation.NavigationDestination

/**
 * Navigation destinations for the Home feature
 */
sealed interface HomeDestination : NavigationDestination {
    data object Main : HomeDestination {
        override val route = "home"
    }
}
