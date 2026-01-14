package com.drinkwater.reminder.features.onboarding.navigation

import com.drinkwater.reminder.core.navigation.NavigationDestination

/**
 * Navigation destinations for Onboarding feature
 */
sealed class OnboardingDestination : NavigationDestination {
    data object Welcome : OnboardingDestination() {
        override val route: String = "onboarding_welcome"
    }

    data object ProfileSetup : OnboardingDestination() {
        override val route: String = "onboarding_profile_setup"
    }
}
