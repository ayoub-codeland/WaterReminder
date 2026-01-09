package com.drinkwater.reminder.features.progress.navigation

import androidx.navigation.NavHostController

/**
 * Navigator for Progress feature
 * Handles ONLY deep navigation within the progress feature
 *
 * Note: Bottom navigation and tab switching is handled at app level (App.kt).
 * BackHandler in ProgressScreen handles back button to navigate to home.
 * This navigator is ONLY for progress sub-screens (if any are added in future).
 * Currently progress has no sub-screens, so this navigator has minimal functionality.
 */
class ProgressNavigator(
    private val navController: NavHostController
) {
    fun navigateToHome() {
        // Called by BackHandler from ProgressScreen
        // Clear entire back stack and navigate to home
        navController.navigate("home_graph") {
            popUpTo(0) { inclusive = true }
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
