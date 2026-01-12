package com.drinkwater.reminder

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.drinkwater.reminder.core.theme.AppTheme
import com.drinkwater.reminder.core.ui.components.AppBottomNavigation
import com.drinkwater.reminder.core.ui.components.AppNavigationTab
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.features.home.navigation.homeGraph
import com.drinkwater.reminder.features.progress.navigation.progressGraph
import com.drinkwater.reminder.features.settings.navigation.settingsGraph
import com.drinkwater.reminder.features.settings.presentation.notifications.RequestNotificationPermissionIfNeeded

/**
 * Main App Composable
 *
 * Following Clean Architecture principles:
 * - Navigation bar is centralized at app level (Single Source of Truth)
 * - Features are decoupled and don't know about each other
 * - Navigation state is derived from NavController
 * - Scalable design for adding new features
 */
@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()

        // Request notification permission on first app launch
        var requestInitialPermission by remember { mutableStateOf(true) }
        RequestNotificationPermissionIfNeeded(
            shouldRequest = requestInitialPermission,
            onPermissionResult = { _ ->
                requestInitialPermission = false
            }
        )

        // Observe current navigation state
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Determine which tab is currently active based on route
        val currentTab = when {
            currentRoute == "home" || currentRoute?.startsWith("home_graph") == true -> AppNavigationTab.HOME
            currentRoute == "progress" || currentRoute?.startsWith("progress_graph") == true -> AppNavigationTab.PROGRESS
            currentRoute?.startsWith("settings") == true -> AppNavigationTab.SETTINGS
            else -> AppNavigationTab.HOME // Default fallback
        }

        // Centralized scaffold with bottom navigation
        AppScaffold(
            bottomBar = {
                AppBottomNavigation(
                    currentTab = currentTab,
                    onNavigateToHome = {
                        // Prevent navigation if already on home tab
                        if (currentTab != AppNavigationTab.HOME) {
                            navController.navigate("home_graph") {
                                // Clear entire back stack - tabs are peers, not hierarchical
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateToProgress = {
                        // Prevent navigation if already on progress tab
                        if (currentTab != AppNavigationTab.PROGRESS) {
                            navController.navigate("progress_graph") {
                                // Clear entire back stack - tabs are peers, not hierarchical
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    onNavigateToSettings = {
                        // Prevent navigation if already on settings tab
                        if (currentTab != AppNavigationTab.SETTINGS) {
                            navController.navigate("settings_graph") {
                                // Clear entire back stack - tabs are peers, not hierarchical
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Navigation host for feature graphs
            NavHost(
                navController = navController,
                startDestination = "home_graph",
                modifier = Modifier.fillMaxSize()
            ) {
                // Home feature (Dashboard)
                homeGraph(
                    navController = navController,
                    route = "home_graph"
                )

                // Progress feature (Analytics)
                progressGraph(
                    navController = navController,
                    route = "progress_graph"
                )

                // Settings feature
                settingsGraph(
                    navController = navController,
                    route = "settings_graph"
                )
            }
        }
    }
}
