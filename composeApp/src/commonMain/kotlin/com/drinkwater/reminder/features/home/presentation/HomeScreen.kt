package com.drinkwater.reminder.features.home.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                progressPercent = state.progressPercent
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
    progressPercent: Int
) {
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
        
        // Progress indicator pill
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
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Goal text
        Text(
            text = "Goal: ${formatAmount(dailyGoal)} ml",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
        color = MaterialTheme.colorScheme.surface
    ) {
        Box {
            // Subtle gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFEFF6FF).copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Daily Tip",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
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
        shape = RoundedCornerShape(16.dp),
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
                    .widthIn(min = 280.dp, max = 340.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Add Water",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Enter amount in milliliters",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Amount input
                OutlinedTextField(
                    value = customAmount,
                    onValueChange = onCustomAmountChanged,
                    placeholder = { Text("300") },
                    suffix = { Text("ml", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = customAmountError != null,
                    supportingText = customAmountError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = onConfirmCustom,
                        enabled = customAmount.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Add Water", fontWeight = FontWeight.SemiBold)
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
