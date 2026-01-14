package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.extensions.shadowSm

/**
 * Update Notification Schedule Screen
 * Uses common AppScaffold and MaterialTheme for consistency
 */
@Composable
fun UpdateNotificationScheduleScreen(
    viewModel: NotificationPreferencesViewModel
) {
    val state by viewModel.state.collectAsState()

    UpdateNotificationScheduleContent(
        currentFrequency = state.frequencyMinutes,
        onFrequencySelected = { frequency ->
            viewModel.onEvent(NotificationPreferencesEvent.OnFrequencySelected(frequency))
        },
        onBackClick = {
            viewModel.onEvent(NotificationPreferencesEvent.OnBackClick)
        },
        onSaveClick = {
            viewModel.onEvent(NotificationPreferencesEvent.OnBackClick)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateNotificationScheduleContent(
    currentFrequency: Int,
    onFrequencySelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // Available notification frequency options (in minutes)
    val frequencyOptions = listOf(15, 30, 45, 60, 90, 120, 180)
    var selectedFrequency by remember(currentFrequency) { mutableStateOf(currentFrequency) }

    AppScaffold(
        topBar = {
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "Notification Frequency",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 100.dp)
                ) {
                    // Description text
                    Text(
                        text = "Choose how often you want to receive hydration reminders. More frequent reminders help you stay on track throughout the day.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )

                    // Frequency options card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .shadowSm(shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column {
                            frequencyOptions.forEachIndexed { index, frequency ->
                                FrequencyOptionItem(
                                    frequency = frequency,
                                    isSelected = selectedFrequency == frequency,
                                    showDivider = index < frequencyOptions.size - 1,
                                    onClick = {
                                        selectedFrequency = frequency
                                    }
                                )
                            }
                        }
                    }
                }

                // Fixed bottom button with gradient
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.background
                                ),
                                startY = 0f,
                                endY = 100f
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .padding(bottom = 16.dp, top = 24.dp)
                ) {
                    Button(
                        onClick = {
                            onFrequencySelected(selectedFrequency)
                            onSaveClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Save Changes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun FrequencyOptionItem(
    frequency: Int,
    isSelected: Boolean,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Surface(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Frequency label
                Text(
                    text = formatFrequencyLabel(frequency),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Check icon (only shown when selected)
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (showDivider) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Format frequency value to human-readable label
 */
private fun formatFrequencyLabel(minutes: Int): String {
    return when {
        minutes < 60 -> "Every $minutes minutes"
        minutes == 60 -> "Every hour"
        minutes % 60 == 0 -> {
            val hours = minutes / 60
            "Every $hours hours"
        }
        else -> {
            val hours = minutes / 60
            val mins = minutes % 60
            "Every ${hours}h ${mins}m"
        }
    }
}
