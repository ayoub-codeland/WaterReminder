package com.drinkwater.reminder.features.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.extensions.shadowSm
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SettingsUiEffect.NavigateToEditProfile -> {
                    // Handle navigation
                }

                is SettingsUiEffect.NavigateToWeightSettings -> {
                    // Handle navigation
                }

                is SettingsUiEffect.NavigateToActivitySettings -> {
                    // Handle navigation
                }

                is SettingsUiEffect.NavigateToGoalSettings -> {
                    // Handle navigation
                }

                is SettingsUiEffect.NavigateToNotifications -> {
                    // Handle navigation
                }

                is SettingsUiEffect.NavigateToStartOfWeek -> {
                    // Handle navigation
                }

                is SettingsUiEffect.OpenUrl -> {
                    // Handle opening URL
                }
            }
        }
    }

    AppScaffold(
        topBar = {
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(
                                onClick = onNavigateBack
                            ) {
                                Text(
                                    text = "Done",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 40.dp)
        ) {
            // Profile Card
            ProfileCard(
                userName = state.userName,
                dailyGoal = state.dailyGoal,
                onEditClick = {
                    viewModel.onEvent(SettingsUiEvent.OnEditProfileClick)
                }
            )

            // Profile & Algorithm Section
            ProfileAlgorithmSection(
                weight = state.weight,
                weightUnit = state.weightUnit,
                activityLevel = state.activityLevel,
                dailyGoal = state.dailyGoal,
                onWeightClick = {
                    viewModel.onEvent(SettingsUiEvent.OnWeightClick)
                },
                onActivityClick = {
                    viewModel.onEvent(SettingsUiEvent.OnActivityLevelClick)
                },
                onDailyGoalClick = {
                    viewModel.onEvent(SettingsUiEvent.OnDailyGoalClick)
                }
            )

            // Notifications Section
            NotificationsSection(
                onNotificationClick = {
                    viewModel.onEvent(SettingsUiEvent.OnNotificationPreferencesClick)
                }
            )

            // App Preferences Section
            AppPreferencesSection(
                volumeUnit = state.volumeUnit,
                startOfWeek = state.startOfWeek,
                onVolumeUnitChanged = { unit ->
                    viewModel.onEvent(SettingsUiEvent.OnVolumeUnitChanged(unit))
                },
                onStartOfWeekClick = {
                    viewModel.onEvent(SettingsUiEvent.OnStartOfWeekClick)
                }
            )

            // About Section
            AboutSection(
                appVersion = state.appVersion,
                onPrivacyPolicyClick = {
                    viewModel.onEvent(SettingsUiEvent.OnPrivacyPolicyClick)
                },
                onTermsClick = {
                    viewModel.onEvent(SettingsUiEvent.OnTermsOfServiceClick)
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

// Placeholder composables (implement these based on your needs)
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
                // Profile Image
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
                        text = "Alex Johnson",
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
                            text = "Goal: 2500ml",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = { },
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
    weight: Int,
    weightUnit: String,
    activityLevel: String,
    dailyGoal: Int,
    onWeightClick: () -> Unit,
    onActivityClick: () -> Unit,
    onDailyGoalClick: () -> Unit
) {
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
                // Weight
                SettingsItem(
                    icon = Icons.Outlined.MonitorWeight,
                    iconBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = "Weight",
                    value = "75 kg",
                    valueColor = MaterialTheme.colorScheme.primary,
                    showDivider = true,
                    onClick = { onWeightClick() }
                )

                // Activity Level
                SettingsItem(
                    icon = Icons.Default.DirectionsRun,
                    iconBackgroundColor = Color(0xFFFFEDD5),
                    iconTint = Color(0xFFF97316),
                    title = "Activity Level",
                    value = "Moderate",
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    showDivider = true,
                    onClick = { onActivityClick() }
                )

                // Daily Goal
                SettingsItem(
                    icon = Icons.Outlined.LocalDrink,
                    iconBackgroundColor = Color(0xFFCFFAFE),
                    iconTint = Color(0xFF06B6D4),
                    title = "Daily Goal",
                    value = "Auto (2500ml)",
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
                onClick = {
                    onNotificationClick
                }
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
                // Start of Week
                SettingsItem(
                    icon = Icons.Outlined.CalendarMonth,
                    iconBackgroundColor = Color(0xFFFEE2E2),
                    iconTint = Color(0xFFEF4444),
                    title = "Start of Week",
                    value = "Monday",
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    showDivider = true,
                    onClick = onStartOfWeekClick
                )

                // Units
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
                        .clickable { }
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
                        .clickable { }
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

        // App Version
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
                text = "HydroTracker v1.0.2",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}