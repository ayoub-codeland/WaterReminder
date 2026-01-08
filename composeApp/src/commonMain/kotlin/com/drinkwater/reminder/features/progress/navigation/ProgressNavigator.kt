package com.drinkwater.reminder.features.progress.navigation

import androidx.navigation.NavHostController

/**
 * Navigator for Progress feature
 */
class ProgressNavigator(
    private val navController: NavHostController
) {
    fun navigateToHome() {
        navController.navigate("home_graph") {
            popUpTo("home_graph") { inclusive = true }
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
            // Can't pop - navigate to home
            navigateToHome()
        }
    }
}
