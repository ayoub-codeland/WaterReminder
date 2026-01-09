package com.drinkwater.reminder.features.progress.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.drinkwater.reminder.core.navigation.HandleEffects
import com.drinkwater.reminder.features.progress.presentation.ProgressScreen
import com.drinkwater.reminder.features.progress.presentation.ProgressSideEffect
import com.drinkwater.reminder.features.progress.presentation.ProgressViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Progress module navigation graph
 */
fun NavGraphBuilder.progressGraph(
    navController: NavHostController,
    route: String = "progress_graph"
) {
    navigation(
        startDestination = ProgressDestination.Main.route,
        route = route
    ) {
        val navigator = ProgressNavigator(navController)
        
        progressMainScreen(navigator)
    }
}

private fun NavGraphBuilder.progressMainScreen(
    navigator: ProgressNavigator
) {
    composable(ProgressDestination.Main.route) {
        val viewModel = koinViewModel<ProgressViewModel>()

        ProgressScreen(
            viewModel = viewModel,
            onNavigateToHome = navigator::navigateToHome
        )

        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                is ProgressSideEffect.ShowError -> {
                    // TODO: Show error snackbar
                }
            }
        }
    }
}
