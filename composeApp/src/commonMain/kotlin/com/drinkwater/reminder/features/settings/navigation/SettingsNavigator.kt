package com.drinkwater.reminder.features.settings.navigation

import androidx.navigation.NavController

class SettingsNavigator(private val navController: NavController) {
    
    fun navigateToMain() {
        navController.navigate(SettingsDestination.Main.route) {
            launchSingleTop = true
        }
    }
    
    fun navigateToHome() {
        navController.navigate("home_graph") {
            popUpTo("home_graph") { inclusive = true }
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
}
