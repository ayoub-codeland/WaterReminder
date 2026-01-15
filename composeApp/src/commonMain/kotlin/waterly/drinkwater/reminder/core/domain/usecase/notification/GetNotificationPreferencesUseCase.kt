package waterly.drinkwater.reminder.core.domain.usecase.notification

import waterly.drinkwater.reminder.core.domain.model.NotificationPreference
import waterly.drinkwater.reminder.core.domain.repository.NotificationRepository

/**
 * Use case to retrieve current notification preferences.
 */
class GetNotificationPreferencesUseCase(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): NotificationPreference? {
        return notificationRepository.getPreferences()
    }
}
