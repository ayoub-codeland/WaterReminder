package com.drinkwater.reminder.features.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.drinkwater.reminder.core.navigation.HandleEffects
import com.drinkwater.reminder.features.onboarding.presentation.profile.ProfileSetupScreen
import com.drinkwater.reminder.features.onboarding.presentation.profile.ProfileSetupUiEffect
import com.drinkwater.reminder.features.onboarding.presentation.profile.ProfileSetupViewModel
import com.drinkwater.reminder.features.onboarding.presentation.welcome.WelcomeScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Onboarding module navigation graph
 * Uses Koin for ViewModel injection
 *
 * Following the same pattern as SettingsNavGraph
 */
fun NavGraphBuilder.onboardingGraph(
    navController: NavHostController,
    route: String = "onboarding_graph",
    onOnboardingComplete: () -> Unit
) {
    navigation(
        startDestination = OnboardingDestination.Welcome.route,
        route = route
    ) {
        welcomeScreen(navController)
        profileSetupScreen(navController, onOnboardingComplete)
    }
}

/**
 * Remember navigator instance for onboarding
 */
@Composable
private fun rememberOnboardingNavigator(
    navController: NavHostController,
    onOnboardingComplete: () -> Unit
): OnboardingNavigator {
    return remember(navController) {
        object : OnboardingNavigator {
            override fun navigateToProfileSetup() {
                navController.navigate(OnboardingDestination.ProfileSetup.route)
            }

            override fun navigateToHome() {
                onOnboardingComplete()
            }

            override fun navigateBack() {
                navController.popBackStack()
            }
        }
    }
}

private fun NavGraphBuilder.welcomeScreen(
    navController: NavHostController
) {
    composable(OnboardingDestination.Welcome.route) {
        val navigator = rememberOnboardingNavigator(navController, {})

        WelcomeScreen(
            onNavigateToProfileSetup = {
                navigator.navigateToProfileSetup()
            }
        )
    }
}

private fun NavGraphBuilder.profileSetupScreen(
    navController: NavHostController,
    onOnboardingComplete: () -> Unit
) {
    composable(OnboardingDestination.ProfileSetup.route) {
        val navigator = rememberOnboardingNavigator(navController, onOnboardingComplete)

        // Inject ViewModel via Koin
        val viewModel = koinViewModel<ProfileSetupViewModel>()

        ProfileSetupScreen(
            viewModel = viewModel,
            onNavigateToDashboard = { _ ->
                // Navigate to home and clear onboarding backstack
                navigator.navigateToHome()
            }
        )

        // Handle navigation effects
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                is ProfileSetupUiEffect.NavigateToDashboard -> {
                    navigator.navigateToHome()
                }
            }
        }
    }
}
