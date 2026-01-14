package com.drinkwater.reminder.features.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.drinkwater.reminder.core.navigation.HandleEffects
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelEffect
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelScreen
import com.drinkwater.reminder.features.settings.presentation.activity.UpdateActivityLevelViewModel
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalEffect
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalScreen
import com.drinkwater.reminder.features.settings.presentation.goal.UpdateDailyGoalViewModel
import com.drinkwater.reminder.features.settings.presentation.main.SettingsEffect
import com.drinkwater.reminder.features.settings.presentation.main.SettingsScreen
import com.drinkwater.reminder.features.settings.presentation.main.SettingsViewModel
import com.drinkwater.reminder.features.settings.presentation.profile.EditProfileEffect
import com.drinkwater.reminder.features.settings.presentation.profile.EditProfileScreen
import com.drinkwater.reminder.features.settings.presentation.profile.EditProfileViewModel
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightEffect
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightScreen
import com.drinkwater.reminder.features.settings.presentation.weight.UpdateWeightViewModel
import com.drinkwater.reminder.features.settings.presentation.notifications.NotificationPreferencesScreen
import com.drinkwater.reminder.features.settings.presentation.notifications.NotificationPreferencesSideEffect
import com.drinkwater.reminder.features.settings.presentation.notifications.NotificationPreferencesViewModel
import com.drinkwater.reminder.features.settings.presentation.share.ShareScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Settings module navigation graph
 * Uses Koin for ViewModel injection
 */
fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    route: String = "settings_graph"
) {
    navigation(
        startDestination = SettingsDestination.Main.route,
        route = route
    ) {
        val navigator = SettingsNavigator(navController)
        
        settingsMainScreen(navigator)
        editProfileScreen(navigator)
        updateWeightScreen(navigator)
        updateActivityScreen(navigator)
        updateGoalScreen(navigator)
        notificationPreferencesScreen(navigator)
        shareScreen(navigator)
    }
}

private fun NavGraphBuilder.settingsMainScreen(
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.Main.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<SettingsViewModel>()

        SettingsScreen(
            viewModel = viewModel,
            onNavigateToHome = navigator::navigateToHome
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                SettingsEffect.NavigateBack -> navigator.navigateBack()
                SettingsEffect.NavigateToEditProfile -> navigator.navigateToEditProfile()
                SettingsEffect.NavigateToWeightSettings -> navigator.navigateToUpdateWeight()
                SettingsEffect.NavigateToActivitySettings -> navigator.navigateToUpdateActivity()
                SettingsEffect.NavigateToGoalSettings -> navigator.navigateToUpdateGoal()
                SettingsEffect.NavigateToNotifications -> navigator.navigateToNotifications()
                SettingsEffect.NavigateToShare -> navigator.navigateToShare()
                SettingsEffect.RateApp -> navigator.rateApp()
                is SettingsEffect.OpenUrl -> navigator.openUrl(effect.url)
            }
        }
    }
}

private fun NavGraphBuilder.editProfileScreen(
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.EditProfile.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<EditProfileViewModel>()
        
        EditProfileScreen(
            viewModel = viewModel,
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                EditProfileEffect.NavigateBack -> navigator.navigateBack()
                is EditProfileEffect.ShowError -> {}
                EditProfileEffect.OpenImagePicker -> {}
            }
        }
    }
}

private fun NavGraphBuilder.updateWeightScreen(
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.UpdateWeight.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<UpdateWeightViewModel>()
        
        UpdateWeightScreen(
            viewModel = viewModel,
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
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.UpdateActivity.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<UpdateActivityLevelViewModel>()
        
        UpdateActivityLevelScreen(
            viewModel = viewModel,
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
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.UpdateGoal.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<UpdateDailyGoalViewModel>()
        
        UpdateDailyGoalScreen(
            viewModel = viewModel,
        )
        
        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                UpdateDailyGoalEffect.NavigateBack -> navigator.navigateBack()
                is UpdateDailyGoalEffect.ShowError -> {}
            }
        }
    }
}

private fun NavGraphBuilder.notificationPreferencesScreen(
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.Notifications.route) {
        // Inject ViewModel via Koin
        val viewModel = koinViewModel<NotificationPreferencesViewModel>()

        NotificationPreferencesScreen(viewModel = viewModel)

        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                NotificationPreferencesSideEffect.NavigateBack -> navigator.navigateBack()
                is NotificationPreferencesSideEffect.ShowError -> {
                    // Error messaging handled by UI layer
                }
                is NotificationPreferencesSideEffect.ShowFrequencyPicker -> {
                    // Frequency picker handled by platform
                }
                is NotificationPreferencesSideEffect.ShowTimePicker -> {
                    // Time picker handled by platform
                }
            }
        }
    }
}

private fun NavGraphBuilder.shareScreen(
    navigator: SettingsNavigator
) {
    composable(SettingsDestination.Share.route) {
        ShareScreen(
            onBackClick = { navigator.navigateBack() },
            onShareClick = { navigator.shareApp() },
            onCopyLinkClick = { navigator.copyLink() }
        )
    }
}
