package com.drinkwater.reminder.features.settings.presentation.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.VolumeUnit
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.components.StandardTopBarCentered
import com.drinkwater.reminder.core.ui.extensions.shadowSm

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    AppScaffold(
        topBar = {
            StandardTopBarCentered(
                title = "Settings"
            )
        },
        bottomBar = {
            SettingsBottomNavigation(
                onHomeClick = { viewModel.onEvent(SettingsEvent.OnBackClick) },
                onProgressClick = { /* Navigate to Progress */ },
                onSettingsClick = { /* Already on Settings */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 24.dp)
        ) {
            ProfileCard(
                userName = state.userName,
                dailyGoal = state.dailyGoal,
                onEditClick = {
                    viewModel.onEvent(SettingsEvent.OnEditProfileClick)
                }
            )

            ProfileAlgorithmSection(
                weight = state.weight,
                weightUnit = state.weightUnit,
                activityLevel = state.activityLevel,
                dailyGoal = state.dailyGoal,
                onWeightClick = {
                    viewModel.onEvent(SettingsEvent.OnWeightClick)
                },
                onActivityClick = {
                    viewModel.onEvent(SettingsEvent.OnActivityLevelClick)
                },
                onDailyGoalClick = {
                    viewModel.onEvent(SettingsEvent.OnDailyGoalClick)
                }
            )

            NotificationsSection(
                onNotificationClick = {
                    viewModel.onEvent(SettingsEvent.OnNotificationPreferencesClick)
                }
            )

            AppPreferencesSection(
                volumeUnit = state.volumeUnit,
                startOfWeek = state.startOfWeek,
                onVolumeUnitChanged = { unit ->
                    viewModel.onEvent(SettingsEvent.OnVolumeUnitChanged(unit))
                },
                onStartOfWeekClick = {
                    viewModel.onEvent(SettingsEvent.OnStartOfWeekClick)
                }
            )

            AboutSection(
                appVersion = state.appVersion,
                onPrivacyPolicyClick = {
                    viewModel.onEvent(SettingsEvent.OnPrivacyPolicyClick)
                },
                onTermsClick = {
                    viewModel.onEvent(SettingsEvent.OnTermsOfServiceClick)
                }
            )
        }
    }
}

@Composable
private fun VolumeUnitToggle(
    selectedUnit: VolumeUnit,
    onUnitSelected: (VolumeUnit) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row {
            Surface(
                onClick = { onUnitSelected(VolumeUnit.ML) },
                color = if (selectedUnit == VolumeUnit.ML)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent,
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
            ) {
                Text(
                    text = "ml",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selectedUnit == VolumeUnit.ML)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Surface(
                onClick = { onUnitSelected(VolumeUnit.OZ) },
                color = if (selectedUnit == VolumeUnit.OZ)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent,
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
            ) {
                Text(
                    text = "oz",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selectedUnit == VolumeUnit.OZ)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    title: String,
    value: String?,
    valueColor: Color?,
    showDivider: Boolean,
    onClick: () -> Unit,
    customTrailingContent: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (customTrailingContent != null) {
                customTrailingContent()
            } else if (value != null && valueColor != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = value,
                        style = if (valueColor == MaterialTheme.colorScheme.primary)
                            MaterialTheme.typography.bodyLarge
                        else
                            MaterialTheme.typography.bodyMedium,
                        color = valueColor
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else if (value == null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF3F4F6)
            )
        }
    }
}

@Composable
private fun ProfileCard(userName: String, dailyGoal: Int, onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Goal: ${dailyGoal}ml",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { onEditClick() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileAlgorithmSection(
    weight: Float,
    weightUnit: WeightUnit,
    activityLevel: ActivityLevel,
    dailyGoal: Int,
    onWeightClick: () -> Unit,
    onActivityClick: () -> Unit,
    onDailyGoalClick: () -> Unit
) {
    val weightContent = "$weight " + when (weightUnit) {
        WeightUnit.KG -> "kg"
        WeightUnit.LBS -> "lbs"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = "PROFILE & ALGORITHM",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsItem(
                    icon = Icons.Outlined.MonitorWeight,
                    iconBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = "Weight",
                    value = weightContent,
                    valueColor = MaterialTheme.colorScheme.primary,
                    showDivider = true,
                    onClick = { onWeightClick() }
                )

                SettingsItem(
                    icon = Icons.Default.DirectionsRun,
                    iconBackgroundColor = Color(0xFFFFEDD5),
                    iconTint = Color(0xFFF97316),
                    title = "Activity Level",
                    value = activityLevel.displayName,
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    showDivider = true,
                    onClick = { onActivityClick() }
                )

                SettingsItem(
                    icon = Icons.Outlined.LocalDrink,
                    iconBackgroundColor = Color(0xFFCFFAFE),
                    iconTint = Color(0xFF06B6D4),
                    title = "Daily Goal",
                    value = "Auto ($dailyGoal ml)",
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    showDivider = false,
                    onClick = { onDailyGoalClick() }
                )
            }
        }

        Text(
            text = "Your hydration goal is calculated based on your weight and activity to optimize your health.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun NotificationsSection(onNotificationClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "NOTIFICATIONS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            SettingsItem(
                icon = Icons.Outlined.NotificationsActive,
                iconBackgroundColor = Color(0xFFDCFCE7),
                iconTint = Color(0xFF16A34A),
                title = "Notification Preferences",
                value = null,
                valueColor = null,
                showDivider = false,
                onClick = onNotificationClick
            )
        }
    }
}

@Composable
private fun AppPreferencesSection(
    volumeUnit: VolumeUnit,
    startOfWeek: String,
    onVolumeUnitChanged: (VolumeUnit) -> Unit,
    onStartOfWeekClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "APP PREFERENCES",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                SettingsItem(
                    icon = Icons.Outlined.CalendarMonth,
                    iconBackgroundColor = Color(0xFFFEE2E2),
                    iconTint = Color(0xFFEF4444),
                    title = "Start of Week",
                    value = startOfWeek,
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    showDivider = true,
                    onClick = onStartOfWeekClick
                )

                SettingsItem(
                    icon = Icons.Default.Straighten,
                    iconBackgroundColor = Color(0xFFCCFBF1),
                    iconTint = Color(0xFF0D9488),
                    title = "Units",
                    value = null,
                    valueColor = null,
                    showDivider = false,
                    customTrailingContent = {
                        VolumeUnitToggle(
                            selectedUnit = volumeUnit,
                            onUnitSelected = onVolumeUnitChanged
                        )
                    },
                    onClick = { onVolumeUnitChanged(volumeUnit) }
                )
            }
        }
    }
}

@Composable
private fun AboutSection(
    appVersion: String,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 32.dp)
    ) {
        Text(
            text = "ABOUT",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadowSm(shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { onPrivacyPolicyClick() }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Outlined.OpenInNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { onTermsClick() }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Terms of Service",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(blue = 0.8f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                text = "HydroTracker v$appVersion",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================================
// Bottom Navigation
// ============================================================================

@Composable
private fun SettingsBottomNavigation(
    onHomeClick: () -> Unit,
    onProgressClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavItem(
                icon = Icons.Filled.Home,
                label = "Home",
                isSelected = false,
                onClick = onHomeClick
            )
            
            BottomNavItem(
                icon = Icons.Filled.BarChart,
                label = "Progress",
                isSelected = false,
                onClick = onProgressClick
            )
            
            BottomNavItem(
                icon = Icons.Filled.Settings,
                label = "Settings",
                isSelected = true,
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            },
            modifier = Modifier.size(26.dp)
        )
        
        Text(
            text = label,
            style = if (isSelected) {
                MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            } else {
                MaterialTheme.typography.labelSmall
            },
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            },
            fontSize = 10.sp
        )
    }
}
