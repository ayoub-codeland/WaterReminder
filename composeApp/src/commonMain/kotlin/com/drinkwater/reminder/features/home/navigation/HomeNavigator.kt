package com.drinkwater.reminder.features.home.navigation

import androidx.navigation.NavHostController

/**
 * Navigator for Home feature
 * Handles navigation actions from the Home screen
 */
class HomeNavigator(
    private val navController: NavHostController
) {
    fun navigateToProgress() {
        navController.navigate("progress_graph") {
            launchSingleTop = true
        }
    }
    
    fun navigateToSettings() {
        navController.navigate("settings_graph") {
            launchSingleTop = true
        }
    }
    
    fun navigateBack() {
        // Safely handle back navigation
        if (!navController.popBackStack()) {
            // Can't pop - stay on home (shouldn't happen on home screen)
            // Do nothing - we're already at root
        }
    }
}
