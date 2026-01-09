package com.drinkwater.reminder.features.progress.navigation

import androidx.navigation.NavHostController

/**
 * Navigator for Progress feature
 *
 * Note: Bottom navigation is handled at app level.
 * This navigator is for deep navigation within the progress feature.
 */
class ProgressNavigator(
    private val navController: NavHostController
) {
    fun navigateToHome() {
        navController.navigate("home_graph") {
            // Clear back stack to home
            popUpTo("home_graph") { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToSettings() {
        navController.navigate("settings_graph") {
            // Keep home in back stack
            popUpTo("home_graph") { inclusive = false }
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        // Safely handle back navigation
        if (!navController.popBackStack()) {
            // Can't pop - navigate to home
            navigateToHome()
        }
    }
}
