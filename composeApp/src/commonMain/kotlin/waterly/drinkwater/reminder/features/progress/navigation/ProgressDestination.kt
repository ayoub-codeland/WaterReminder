package waterly.drinkwater.reminder.features.progress.navigation

import waterly.drinkwater.reminder.core.navigation.NavigationDestination

/**
 * Navigation destinations for the Progress feature
 */
sealed interface ProgressDestination : NavigationDestination {
    data object Main : ProgressDestination {
        override val route = "progress"
    }
}
