package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.extensions.shadowSm

/**
 * Notification Preferences Screen - Settings sub-screen
 * UI layer: Pure presentation, renders state from ViewModel
 */
@Composable
fun NotificationPreferencesScreen(
    viewModel: NotificationPreferencesViewModel
) {
    val state by viewModel.state.collectAsState()

    NotificationPreferencesScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationPreferencesScreenContent(
    state: NotificationPreferencesState,
    onEvent: (NotificationPreferencesEvent) -> Unit
) {
    AppScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notification Preferences",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(NotificationPreferencesEvent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Enable Hydration Reminders Toggle
                EnableNotificationsCard(
                    isEnabled = state.isEnabled,
                    onToggle = { onEvent(NotificationPreferencesEvent.OnToggleNotifications(it)) }
                )

                // Schedule Section
                ScheduleSection(
                    frequencyMinutes = state.frequencyMinutes,
                    wakeUpTime = state.wakeUpTime,
                    bedtime = state.bedtime,
                    onFrequencyClick = { onEvent(NotificationPreferencesEvent.OnFrequencyClick) },
                    onWakeUpTimeClick = { onEvent(NotificationPreferencesEvent.OnWakeUpTimeClick) },
                    onBedtimeClick = { onEvent(NotificationPreferencesEvent.OnBedtimeClick) }
                )

                // Smart Options Section
                SmartOptionsSection(
                    pauseWhenGoalReached = state.pauseWhenGoalReached,
                    onTogglePause = { onEvent(NotificationPreferencesEvent.OnTogglePauseWhenGoalReached(it)) }
                )
            }
        }
    )
}

@Composable
private fun EnableNotificationsCard(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadowSm(shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enable Hydration Reminders",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Composable
private fun ScheduleSection(
    frequencyMinutes: Int,
    wakeUpTime: String,
    bedtime: String,
    onFrequencyClick: () -> Unit,
    onWakeUpTimeClick: () -> Unit,
    onBedtimeClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Section header
        Text(
            text = "SCHEDULE",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
        )

        // Schedule card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column {
                // Frequency
                ScheduleItem(
                    icon = Icons.Outlined.Schedule,
                    iconBackgroundColor = Color(0xFFEFF6FF),
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = "Frequency",
                    value = "Every $frequencyMinutes mins",
                    showDivider = true,
                    onClick = onFrequencyClick
                )

                // Wake Up Time
                ScheduleTimeItem(
                    icon = Icons.Outlined.WbSunny,
                    iconBackgroundColor = Color(0xFFFFF7ED),
                    iconTint = Color(0xFFF97316),
                    title = "Wake Up Time",
                    time = wakeUpTime,
                    showDivider = true,
                    onClick = onWakeUpTimeClick
                )

                // Bedtime
                ScheduleTimeItem(
                    icon = Icons.Outlined.Bedtime,
                    iconBackgroundColor = Color(0xFFEEF2FF),
                    iconTint = Color(0xFF6366F1),
                    title = "Bedtime",
                    time = bedtime,
                    showDivider = false,
                    onClick = onBedtimeClick
                )
            }
        }

        // Helper text
        Text(
            text = "Notifications will be paused between bedtime and wake up time.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
    }
}

@Composable
private fun ScheduleItem(
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    title: String,
    value: String,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Surface(
            onClick = onClick,
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = iconBackgroundColor
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(20.dp)
                        )
                    }

                    // Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Value and chevron
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                        imageVector = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun ScheduleTimeItem(
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    title: String,
    time: String,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = iconBackgroundColor
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(20.dp)
                    )
                }

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Time button
            Surface(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun SmartOptionsSection(
    pauseWhenGoalReached: Boolean,
    onTogglePause: (Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Section header
        Text(
            text = "SMART OPTIONS",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Smart options card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Pause when goal reached",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Stops sending reminders once you've hit your daily target.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Switch(
                    checked = pauseWhenGoalReached,
                    onCheckedChange = onTogglePause,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
    }
}
