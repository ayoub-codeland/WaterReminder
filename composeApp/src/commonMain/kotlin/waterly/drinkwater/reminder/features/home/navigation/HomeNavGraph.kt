package waterly.drinkwater.reminder.features.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import waterly.drinkwater.reminder.core.navigation.HandleEffects
import waterly.drinkwater.reminder.features.home.presentation.HomeScreen
import waterly.drinkwater.reminder.features.home.presentation.HomeSideEffect
import waterly.drinkwater.reminder.features.home.presentation.HomeViewModel
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
            viewModel = viewModel
        )

        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                HomeSideEffect.ShowGoalReachedCelebration -> {
                    // Celebration animation handled by UI layer
                }
                is HomeSideEffect.ShowError -> {
                    // Error messaging handled by UI layer
                }
                is HomeSideEffect.ShowWaterAddedFeedback -> {
                    // User feedback handled by platform
                }
            }
        }
    }
}
