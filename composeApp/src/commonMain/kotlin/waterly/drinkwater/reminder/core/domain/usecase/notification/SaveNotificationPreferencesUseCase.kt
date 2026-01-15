package waterly.drinkwater.reminder.core.domain.usecase.notification

import waterly.drinkwater.reminder.core.domain.model.NotificationPreference
import waterly.drinkwater.reminder.core.domain.repository.NotificationRepository

/**
 * Use case to save notification preferences and reschedule notifications.
 *
 * This will:
 * 1. Persist preferences to DataStore
 * 2. Schedule notifications if enabled
 * 3. Cancel notifications if disabled
 */
class SaveNotificationPreferencesUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(preference: NotificationPreference) {
        notificationRepository.savePreferences(preference)
    }
}
