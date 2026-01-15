package waterly.drinkwater.reminder.features.settings.navigation

import waterly.drinkwater.reminder.core.navigation.NavigationDestination

sealed interface SettingsDestination : NavigationDestination {
    data object Main : SettingsDestination {
        override val route = "settings"
    }
    
    data object EditProfile : SettingsDestination {
        override val route = "settings/edit_profile"
    }
    
    data object UpdateWeight : SettingsDestination {
        override val route = "settings/update_weight"
    }
    
    data object UpdateActivity : SettingsDestination {
        override val route = "settings/update_activity"
    }
    
    data object UpdateGoal : SettingsDestination {
        override val route = "settings/update_goal"
    }

    data object Notifications : SettingsDestination {
        override val route = "settings/notifications"
    }

    data object Share : SettingsDestination {
        override val route = "settings/share"
    }

    data object UpdateNotificationSchedule : SettingsDestination {
        override val route = "settings/update_notification_schedule"
    }
}
