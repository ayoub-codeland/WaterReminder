package com.drinkwater.reminder.features.home.navigation

import androidx.navigation.NavHostController

/**
 * Navigator for Home feature
 * Handles ONLY deep navigation within the home feature
 *
 * Note: Bottom navigation and tab switching is handled at app level (App.kt).
 * This navigator is ONLY for home sub-screens (if any are added in future).
 * Currently home has no sub-screens, so this navigator has minimal functionality.
 */
class HomeNavigator(
    private val navController: NavHostController
) {
    fun navigateBack() {
        // Safely handle back navigation
        if (!navController.popBackStack()) {
            // Can't pop - stay on home (shouldn't happen on home screen)
            // Do nothing - we're already at root
        }
    }
}
