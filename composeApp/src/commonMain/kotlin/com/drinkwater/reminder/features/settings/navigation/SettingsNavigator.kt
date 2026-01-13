package com.drinkwater.reminder.features.settings.navigation

import androidx.navigation.NavController

/**
 * Navigator for Settings feature
 * Handles ONLY deep navigation within the settings feature
 *
 * Note: Bottom navigation and tab switching is handled at app level.
 * This navigator is ONLY for settings sub-screens (Edit Profile, Update Weight, etc.)
 */
class SettingsNavigator(private val navController: NavController) {

    fun navigateToHome() {
        // Called by BackHandler from SettingsScreen main screen
        // Clear entire back stack and navigate to home
        navController.navigate("home_graph") {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateToEditProfile() {
        navController.navigate(SettingsDestination.EditProfile.route) {
            launchSingleTop = true
        }
    }

    fun navigateToUpdateWeight() {
        navController.navigate(SettingsDestination.UpdateWeight.route) {
            launchSingleTop = true
        }
    }

    fun navigateToUpdateActivity() {
        navController.navigate(SettingsDestination.UpdateActivity.route) {
            launchSingleTop = true
        }
    }

    fun navigateToUpdateGoal() {
        navController.navigate(SettingsDestination.UpdateGoal.route) {
            launchSingleTop = true
        }
    }

    fun navigateToNotifications() {
        navController.navigate(SettingsDestination.Notifications.route) {
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        // Safely handle back navigation - prevent blank screens
        if (!navController.popBackStack()) {
            // Can't pop anymore - navigate to home instead of showing blank
            navController.navigate("home_graph") {
                popUpTo("home_graph") { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    fun openUrl(url: String) {
        // TODO: Platform-specific URL opening
    }

    fun shareApp() {
        // TODO: Platform-specific app sharing
        // Opens native share sheet with app link
    }

    fun rateApp() {
        // TODO: Platform-specific app rating
        // Opens Play Store/App Store rating page
    }
}
