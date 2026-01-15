package waterly.drinkwater.reminder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import waterly.drinkwater.reminder.core.theme.AppTheme
import waterly.drinkwater.reminder.core.ui.components.AppBottomNavigation
import waterly.drinkwater.reminder.core.ui.components.AppNavigationTab
import waterly.drinkwater.reminder.core.ui.components.AppScaffold
import waterly.drinkwater.reminder.features.home.navigation.homeGraph
import waterly.drinkwater.reminder.features.main.AppUiState
import waterly.drinkwater.reminder.features.main.AppViewModel
import waterly.drinkwater.reminder.features.onboarding.navigation.onboardingGraph
import waterly.drinkwater.reminder.features.progress.navigation.progressGraph
import waterly.drinkwater.reminder.features.settings.navigation.settingsGraph
import waterly.drinkwater.reminder.features.settings.presentation.notifications.RequestNotificationPermissionIfNeeded
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    AppTheme {
        val viewModel: AppViewModel = koinViewModel()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        var requestInitialPermission by remember { mutableStateOf(true) }
        RequestNotificationPermissionIfNeeded(
            shouldRequest = requestInitialPermission,
            onPermissionResult = { requestInitialPermission = false }
        )

        when (val state = uiState) {
            is AppUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AppUiState.Ready -> {
                AppContent(startDestination = state.startDestination)
            }
        }
    }
}

/**
 * Extracted AppContent to keep the main App composable clean and focused on state switching.
 * This component is responsible strictly for Navigation structure.
 */
@Composable
private fun AppContent(startDestination: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentTab = remember(currentRoute) {
        when {
            currentRoute == "home" || currentRoute?.startsWith("home_graph") == true -> AppNavigationTab.HOME
            currentRoute == "progress" || currentRoute?.startsWith("progress_graph") == true -> AppNavigationTab.PROGRESS
            currentRoute?.startsWith("settings") == true -> AppNavigationTab.SETTINGS
            else -> AppNavigationTab.HOME
        }
    }

    val isOnboarding = currentRoute?.startsWith("onboarding") == true

    AppScaffold(
        bottomBar = {
            if (!isOnboarding) {
                AppBottomNavigation(
                    currentTab = currentTab,
                    onNavigateToHome = {
                        navController.navigate("home_graph") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToProgress = {
                        navController.navigate("progress_graph") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings_graph") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            onboardingGraph(
                navController = navController,
                route = "onboarding_graph",
                onOnboardingComplete = {
                    navController.navigate("home_graph") {
                        popUpTo("onboarding_graph") { inclusive = true }
                    }
                }
            )

            homeGraph(navController = navController, route = "home_graph")
            progressGraph(navController = navController, route = "progress_graph")
            settingsGraph(navController = navController, route = "settings_graph")
        }
    }
}