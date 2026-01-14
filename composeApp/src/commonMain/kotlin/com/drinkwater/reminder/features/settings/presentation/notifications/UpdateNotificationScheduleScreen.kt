package com.drinkwater.reminder.features.settings.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.extensions.shadowSm

/**
 * Update Notification Schedule Screen
 * Matches HTML design 1:1 from code.html
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7F8))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF5F7F8)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = (-8).dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Title
                    Text(
                        text = "Notification Frequency",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )

                    // Spacer for symmetry
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }

            // Main content with bottom padding for button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
            ) {
                // Description text
                Text(
                    text = "Choose how often you want to receive hydration reminders. More frequent reminders help you stay on track throughout the day.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )

                // Frequency options card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                        .shadowSm(shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFE2E8F0)
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
        }

        // Fixed bottom button with gradient
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F7F8).copy(alpha = 0f),
                            Color(0xFFF5F7F8),
                            Color(0xFFF5F7F8)
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
                    containerColor = Color(0xFF259DF4)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    text = "Save Changes",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
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
            color = Color.Transparent
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0F172A)
                )

                // Check icon (only shown when selected)
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = Color(0xFF259DF4),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (showDivider) {
            HorizontalDivider(
                color = Color(0xFFE2E8F0),
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
