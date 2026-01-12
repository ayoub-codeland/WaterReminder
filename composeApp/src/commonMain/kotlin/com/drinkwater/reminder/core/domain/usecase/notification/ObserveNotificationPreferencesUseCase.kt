package com.drinkwater.reminder.core.domain.usecase.notification

import com.drinkwater.reminder.core.domain.model.NotificationPreference
import com.drinkwater.reminder.core.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to observe notification preferences changes.
 *
 * Returns a Flow that emits whenever preferences are updated.
 */
class ObserveNotificationPreferencesUseCase(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<NotificationPreference?> {
        return notificationRepository.observePreferences()
    }
}
