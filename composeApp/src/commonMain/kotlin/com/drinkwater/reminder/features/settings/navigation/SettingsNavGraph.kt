package com.drinkwater.reminder.features.settings.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelEffect
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelScreen
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalEffect
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalScreen
import com.drinkwater.reminder.features.settings.presentation.main.SettingsEffect
import com.drinkwater.reminder.features.settings.presentation.main.SettingsScreen
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightEffect
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightScreen

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    route: String = "settings_graph"
) {
    navigation(
        startDestination = SettingsDestination.Main.route,
        route = route
    ) {
        val navigator = SettingsNavigator(navController)
        val viewModelFactory = SettingsViewModelFactory()
        
        settingsMainScreen(navigator, viewModelFactory)
        updateWeightScreen(navigator, viewModelFactory)
        updateActivityScreen(navigator, viewModelFactory)
        updateGoalScreen(navigator, viewModelFactory)
    }
}

private fun NavGraphBuilder.settingsMainScreen(
    navigator: SettingsNavigator,
    viewModelFactory: SettingsViewModelFactory
) {
    composable(SettingsDestination.Main.route) {
        val viewModel = remember { viewModelFactory.createSettingsViewModel() }
        
        SettingsScreen(
            viewModel = viewModel,
            onNavigateBack = navigator::navigateBack
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                SettingsEffect.NavigateBack -> navigator.navigateBack()
                SettingsEffect.NavigateToWeightSettings -> navigator.navigateToUpdateWeight()
                SettingsEffect.NavigateToActivitySettings -> navigator.navigateToUpdateActivity()
                SettingsEffect.NavigateToGoalSettings -> navigator.navigateToUpdateGoal()
                SettingsEffect.NavigateToEditProfile -> {}
                SettingsEffect.NavigateToNotifications -> {}
                SettingsEffect.NavigateToStartOfWeek -> {}
                is SettingsEffect.OpenUrl -> navigator.openUrl(effect.url)
            }
        }
    }
}

private fun NavGraphBuilder.updateWeightScreen(
    navigator: SettingsNavigator,
    viewModelFactory: SettingsViewModelFactory
) {
    composable(SettingsDestination.UpdateWeight.route) {
        val viewModel = remember { viewModelFactory.createUpdateWeightViewModel() }
        
        UpdateWeightScreen(
            viewModel = viewModel,
            onNavigateBack = navigator::navigateBack
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                UpdateWeightEffect.NavigateBack -> navigator.navigateBack()
                is UpdateWeightEffect.ShowError -> {}
            }
        }
    }
}

private fun NavGraphBuilder.updateActivityScreen(
    navigator: SettingsNavigator,
    viewModelFactory: SettingsViewModelFactory
) {
    composable(SettingsDestination.UpdateActivity.route) {
        val viewModel = remember { viewModelFactory.createUpdateActivityViewModel() }
        
        UpdateActivityLevelScreen(
            viewModel = viewModel,
            onNavigateBack = navigator::navigateBack
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                UpdateActivityLevelEffect.NavigateBack -> navigator.navigateBack()
                is UpdateActivityLevelEffect.ShowError -> {}
            }
        }
    }
}

private fun NavGraphBuilder.updateGoalScreen(
    navigator: SettingsNavigator,
    viewModelFactory: SettingsViewModelFactory
) {
    composable(SettingsDestination.UpdateGoal.route) {
        val viewModel = remember { viewModelFactory.createUpdateGoalViewModel() }
        
        UpdateDailyGoalScreen(
            viewModel = viewModel,
            onNavigateBack = navigator::navigateBack
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                UpdateDailyGoalEffect.NavigateBack -> navigator.navigateBack()
                is UpdateDailyGoalEffect.ShowError -> {}
            }
        }
    }
}
