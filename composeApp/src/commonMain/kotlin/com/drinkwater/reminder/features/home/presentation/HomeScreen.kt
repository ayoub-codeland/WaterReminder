package com.drinkwater.reminder.features.home.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.drinkwater.reminder.core.ui.extensions.shadowSm
import com.drinkwater.reminder.features.home.presentation.components.WaterCupComponent
import org.jetbrains.compose.resources.painterResource
import waterreminderapp.composeapp.generated.resources.Res
import waterreminderapp.composeapp.generated.resources.tip_bg

/**
 * Home (Dashboard) Screen
 *
 * Main screen showing water intake progress with WaterCupComponent
 * Navigation bar is handled at app level for consistency across all screens
 *
 * Note: Tab navigation is handled at app level (App.kt), not via callbacks.
 * This keeps the feature decoupled and follows Clean Architecture principles.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeTopBar(
            currentDate = state.currentDate,
            greeting = state.greeting,
            userName = state.userName
        )

        HomeContent(
            state = state,
            onEvent = viewModel::onEvent
        )
    }

    // Add Water Dialog
    if (state.showAddWaterDialog) {
        AddWaterDialog(
            currentIntake = state.currentIntake,
            dailyGoal = state.dailyGoal,
            customAmount = state.customWaterAmount,
            customAmountError = state.customAmountError,
            onCustomAmountChanged = { viewModel.onEvent(HomeEvent.OnCustomAmountChanged(it)) },
            onAddPreset = { amount -> viewModel.onEvent(HomeEvent.OnAddWater(amount)) },
            onConfirmCustom = { viewModel.onEvent(HomeEvent.OnConfirmCustomAmount) },
            onDismiss = { viewModel.onEvent(HomeEvent.OnDismissAddWaterDialog) },
            onReset = { viewModel.onEvent(HomeEvent.OnResetIntake) }
        )
    }
}

@Composable
private fun HomeTopBar(
    currentDate: String,
    greeting: String,
    userName: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = currentDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "$greeting, $userName",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    // Background glow effect
    Box(modifier = Modifier.fillMaxSize()) {
        // Decorative blur circle (matches HTML reference)
        Box(
            modifier = Modifier
                .offset(x = (-50).dp, y = (-100).dp)
                .size(300.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Streak Badge
            StreakBadge(streakDays = state.currentStreak)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Water Cup Component - CENTER PIECE
            WaterCupSection(
                currentIntake = state.currentIntake,
                dailyGoal = state.dailyGoal,
                progressPercent = state.progressPercent,
                onReset = { onEvent(HomeEvent.OnResetIntake) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Daily Tip Card
            AnimatedVisibility(
                visible = state.showDailyTip,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                DailyTipCard(
                    tip = state.dailyTip,
                    onDismiss = { onEvent(HomeEvent.OnDismissDailyTip) }
                )
            }
            
            if (state.showDailyTip) {
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Quick Add Section
            QuickAddSection(
                onAddGlass = { onEvent(HomeEvent.OnAddGlass) },
                onAddBottle = { onEvent(HomeEvent.OnAddBottle) },
                onCustomClick = { onEvent(HomeEvent.OnAddWaterClick) }
            )
        }
    }
}

@Composable
private fun StreakBadge(streakDays: Int) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFFFF7ED),
        border = BorderStroke(1.dp, Color(0xFFFFEDD5))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFF97316),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "$streakDays Day Streak",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFEA580C),
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun WaterCupSection(
    currentIntake: Int,
    dailyGoal: Int,
    progressPercent: Int,
    onReset: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Water Cup - Clean display without any container/background
        WaterCupComponent(
            currentAmount = currentIntake,
            goalAmount = dailyGoal,
            cupWidth = 220.dp,
            cupHeight = 280.dp,
            showTestPopup = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress indicator pill with reset button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "$progressPercent% Complete",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Reset button - subtle and elegant, follows app theme
            if (currentIntake > 0) {
                Surface(
                    onClick = { showResetDialog = true },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset water intake",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Goal text
        Text(
            text = "Goal: ${formatAmount(dailyGoal)} ml",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    // Reset confirmation dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = "Reset Today's Water Intake?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will reset your water intake for today to 0ml. Your streak and history will remain intact.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReset()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun DailyTipCard(
    tip: String,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadowSm(shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background image with opacity filter (matches HTML: opacity-10)
            androidx.compose.foundation.Image(
                painter = painterResource(Res.drawable.tip_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.10f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            // Gradient overlay (matches HTML: bg-gradient-to-r from-blue-50/50 to-transparent)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFEFF6FF).copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Header with icon and label
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "DAILY TIP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Tip text (matches HTML: text-slate-900 text-base font-medium)
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = Color(0xFF0F172A)
                    )
                }

                // Close button (matches HTML: text-slate-400 hover:text-slate-600)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickAddSection(
    onAddGlass: () -> Unit,
    onAddBottle: () -> Unit,
    onCustomClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Quick Add",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Quick add buttons grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAddButton(
                label = "Glass",
                amount = "250 ml",
                icon = Icons.Outlined.WaterDrop,
                onClick = onAddGlass,
                modifier = Modifier.weight(1f)
            )
            
            QuickAddButton(
                label = "Bottle",
                amount = "500 ml",
                icon = Icons.Outlined.CheckCircle,
                onClick = onAddBottle,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Custom button
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onCustomClick),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Custom",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QuickAddButton(
    label: String,
    amount: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = amount,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================================
// Add Water Dialog
// ============================================================================

@Composable
private fun AddWaterDialog(
    currentIntake: Int,
    dailyGoal: Int,
    customAmount: String,
    customAmountError: String?,
    onCustomAmountChanged: (String) -> Unit,
    onAddPreset: (Int) -> Unit,
    onConfirmCustom: () -> Unit,
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 280.dp, max = 380.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Add Water",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle
                Text(
                    text = "Enter amount in milliliters",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Input section with +/- buttons (following HTML design)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus button
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        IconButton(
                            onClick = {
                                val current = customAmount.toIntOrNull() ?: 0
                                if (current > 0) {
                                    onCustomAmountChanged((current - 50).coerceAtLeast(0).toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Input field with ml suffix (following HTML design exactly)
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = customAmount,
                            onValueChange = { newValue ->
                                // Limit to 3000ml maximum (scientific safety limit)
                                val amount = newValue.toIntOrNull()
                                if (amount == null || amount <= 3000) {
                                    onCustomAmountChanged(newValue)
                                }
                            },
                            textStyle = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(96.dp),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (customAmount.isEmpty()) {
                                        Text(
                                            text = "300",
                                            style = MaterialTheme.typography.displaySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                            ),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "ml",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Plus button
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        IconButton(
                            onClick = {
                                val current = customAmount.toIntOrNull() ?: 0
                                val newAmount = (current + 50).coerceAtMost(3000)
                                onCustomAmountChanged(newAmount.toString())
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Action buttons (following HTML design)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = onConfirmCustom,
                        enabled = customAmount.isNotBlank() && (customAmount.toIntOrNull() ?: 0) > 0,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Add Water",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ============================================================================
// Utility Functions
// ============================================================================

private fun formatAmount(amount: Int): String {
    return if (amount >= 1000) {
        val thousands = amount / 1000
        val remainder = amount % 1000
        if (remainder == 0) "$thousands,000"
        else "$thousands,${remainder.toString().padStart(3, '0')}"
    } else amount.toString()
}
