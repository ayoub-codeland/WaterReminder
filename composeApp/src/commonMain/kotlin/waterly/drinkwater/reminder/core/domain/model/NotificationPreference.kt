package waterly.drinkwater.reminder.core.domain.model

/**
 * Domain model representing user's notification preferences for water reminders.
 *
 * @property isEnabled Whether water reminder notifications are enabled
 * @property frequencyMinutes How often to send reminders (in minutes)
 * @property wakeUpTime Time when reminders should start (24-hour format, e.g., "07:00")
 * @property bedtime Time when reminders should stop (24-hour format, e.g., "22:30")
 * @property pauseWhenGoalReached Whether to pause reminders when daily goal is reached
 */
data class NotificationPreference(
    val isEnabled: Boolean = true,
    val frequencyMinutes: Int = 60,
    val wakeUpTime: String = "07:00",
    val bedtime: String = "22:30",
    val pauseWhenGoalReached: Boolean = true
)
