package waterly.drinkwater.reminder.features.onboarding.navigation

/**
 * Navigator interface for Onboarding feature
 * Defines all possible navigation actions within the onboarding flow
 */
interface OnboardingNavigator {
    fun navigateToProfileSetup()
    fun navigateToHome()
    fun navigateBack()
}
