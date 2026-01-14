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
import com.drinkwater.reminder.features.settings.presentation.notifications.UpdateNotificationScheduleScreen
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
        settingsMainScreen(navController)
        editProfileScreen(navController)
        updateWeightScreen(navController)
        updateActivityScreen(navController)
        updateGoalScreen(navController)
        notificationPreferencesScreen(navController)
        updateNotificationScheduleScreen(navController)
        shareScreen(navController)
    }
}

private fun NavGraphBuilder.settingsMainScreen(
    navController: NavHostController
) {
    composable(SettingsDestination.Main.route) {
        val navigator = rememberSettingsNavigator(navController)

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
    navController: NavHostController
) {
    composable(SettingsDestination.EditProfile.route) {
        val navigator = rememberSettingsNavigator(navController)

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
    navController: NavHostController
) {
    composable(SettingsDestination.UpdateWeight.route) {
        val navigator = rememberSettingsNavigator(navController)

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
    navController: NavHostController
) {
    composable(SettingsDestination.UpdateActivity.route) {
        val navigator = rememberSettingsNavigator(navController)

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
    navController: NavHostController
) {
    composable(SettingsDestination.UpdateGoal.route) {
        val navigator = rememberSettingsNavigator(navController)

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
    navController: NavHostController
) {
    composable(SettingsDestination.Notifications.route) {
        val navigator = rememberSettingsNavigator(navController)

        // Inject ViewModel via Koin
        val viewModel = koinViewModel<NotificationPreferencesViewModel>()

        NotificationPreferencesScreen(viewModel = viewModel)

        HandleEffects(viewModel.effect) { effect ->
            when (effect) {
                NotificationPreferencesSideEffect.NavigateBack -> navigator.navigateBack()
                NotificationPreferencesSideEffect.NavigateToUpdateNotificationSchedule -> {
                    navigator.navigateToUpdateNotificationSchedule()
                }
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

private fun NavGraphBuilder.updateNotificationScheduleScreen(
    navController: NavHostController
) {
    composable(SettingsDestination.UpdateNotificationSchedule.route) {
        val viewModel = koinViewModel<NotificationPreferencesViewModel>()

        UpdateNotificationScheduleScreen(viewModel = viewModel)
    }
}

private fun NavGraphBuilder.shareScreen(
    navController: NavHostController
) {
    composable(SettingsDestination.Share.route) {
        val navigator = rememberSettingsNavigator(navController)

        ShareScreen(
            onBackClick = { navigator.navigateBack() },
            onShareClick = { navigator.shareApp() },
            onCopyLinkClick = { navigator.copyLink() }
        )
    }
}
