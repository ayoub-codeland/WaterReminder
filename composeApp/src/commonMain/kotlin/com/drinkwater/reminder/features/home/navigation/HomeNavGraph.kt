package com.drinkwater.reminder.features.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.drinkwater.reminder.core.navigation.HandleEffects
import com.drinkwater.reminder.features.home.presentation.HomeScreen
import com.drinkwater.reminder.features.home.presentation.HomeSideEffect
import com.drinkwater.reminder.features.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home module navigation graph
 * Uses Koin for ViewModel injection
 */
fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    route: String = "home_graph"
) {
    navigation(
        startDestination = HomeDestination.Main.route,
        route = route
    ) {
        val navigator = HomeNavigator(navController)
        
        homeMainScreen(navigator)
    }
}

private fun NavGraphBuilder.homeMainScreen(
    navigator: HomeNavigator
) {
    composable(HomeDestination.Main.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<HomeViewModel>()
        
        HomeScreen(
            viewModel = viewModel,
            onNavigateToProgress = navigator::navigateToProgress,
            onNavigateToSettings = navigator::navigateToSettings
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                HomeSideEffect.NavigateToProgress -> navigator.navigateToProgress()
                HomeSideEffect.NavigateToSettings -> navigator.navigateToSettings()
                HomeSideEffect.ShowGoalReachedCelebration -> {
                    // TODO: Show celebration animation or dialog
                }
                is HomeSideEffect.ShowError -> {
                    // TODO: Show error snackbar/toast
                }
                is HomeSideEffect.ShowWaterAddedFeedback -> {
                    // TODO: Show feedback (haptic, sound, etc.)
                }
            }
        }
    }
}
