package waterly.drinkwater.reminder.features.progress.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import waterly.drinkwater.reminder.core.ui.extensions.shadowSm
import kotlin.math.roundToInt


/**
 * Progress Screen - Analytics and statistics view
 * Navigation bar is handled at app level for consistency across all screens
 */
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel,
    onNavigateToHome: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    // Intercept back button - navigate to home instead of cycling through tabs
    androidx.activity.compose.BackHandler {
        onNavigateToHome()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProgressTopBar()

        ProgressContent(
            state = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressTopBar() {
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
                Text(
                    text = "Progress",
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
}

@Composable
private fun ProgressContent(
    state: ProgressState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Weekly Summary Section
        WeeklySummarySection(
            weeklyTotal = state.weeklyTotal,
            percentageChange = state.percentageChange,
            currentStreak = state.currentStreak,
            chartData = state.chartData
        )

        // Stats Grid
        StatsGrid(
            dailyAverage = state.dailyAverage,
            dailyAverageChange = state.dailyAverageChange,
            allTimeTotal = state.allTimeTotal,
            allTimeSince = state.allTimeSince,
            bestDay = state.bestDay,
            bestDayAmount = state.bestDayAmount,
            completionRate = state.completionRate,
            daysMetGoal = state.daysMetGoal
        )

        // Smart Tip
        SmartTipCard(tip = state.smartTip)
    }
}

@Composable
private fun WeeklySummarySection(
    weeklyTotal: String,
    percentageChange: Int?,
    currentStreak: Int,
    chartData: List<DayChartData>
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "Weekly Hydration",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = weeklyTotal,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Percentage change badge
                percentageChange?.let { change ->
                    val isPositive = change >= 0
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isPositive) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        border = BorderStroke(
                            1.dp,
                            if (isPositive) Color(0xFFBBF7D0) else Color(0xFFFECACA)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = if (isPositive) Color(0xFF16A34A) else Color(0xFFDC2626),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${if (isPositive) "+" else ""}$change%",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isPositive) Color(0xFF16A34A) else Color(0xFFDC2626)
                            )
                        }
                    }
                }

                // Streak badge
                if (currentStreak > 0) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFFF7ED),
                        border = BorderStroke(1.dp, Color(0xFFFFEDD5))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFF97316),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "$currentStreak Day Streak",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF97316)
                            )
                        }
                    }
                }
            }
        }

        // Weekly Chart
        WeeklyChart(chartData = chartData)
    }
}

@Composable
private fun WeeklyChart(chartData: List<DayChartData>) {
    var selectedDay by remember { mutableStateOf<DayChartData?>(null) }

    // Calculate max water intake in the week for scaling bars
    // Use max of actual data OR typical daily goal (2500ml) to ensure reasonable scaling
    val maxMlInWeek = chartData
        .filter { !it.isFuture }
        .maxOfOrNull { it.totalMl }
        ?.coerceAtLeast(2500) ?: 2500

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadowSm(shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selected day details (shown when user taps a bar)
            selectedDay?.let { day ->
                DayDetailsCard(
                    dayData = day,
                    onDismiss = { selectedDay = null }
                )
            }

            // Bars representing absolute water intake
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                chartData.forEach { dayData ->
                    DayBar(
                        data = dayData,
                        isSelected = selectedDay?.dayLabel == dayData.dayLabel,
                        onClick = {
                            selectedDay = if (selectedDay?.dayLabel == dayData.dayLabel) {
                                null // Deselect if already selected
                            } else {
                                dayData
                            }
                        },
                        maxMlInWeek = maxMlInWeek
                    )
                }
            }
        }
    }
}

@Composable
private fun DayDetailsCard(
    dayData: DayChartData,
    onDismiss: () -> Unit
) {
    val liters = dayData.totalMl / 1000f
    val goalLiters = dayData.goalMl / 1000f
    val percentage = if (dayData.goalMl > 0) {
        ((dayData.totalMl.toFloat() / dayData.goalMl.toFloat()) * 100).roundToInt()
    } else 0

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day info
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dayData.dayName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Context badges (data comes from State via ViewModel)
                    if (dayData.isToday) {
                        ContextBadge(
                            text = "Today",
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    if (dayData.isBestDay) {
                        ContextBadge(
                            text = "Best Day",
                            backgroundColor = Color(0xFFDCFCE7),
                            textColor = Color(0xFF16A34A)
                        )
                    }

                    if (dayData.isWorstDay) {
                        ContextBadge(
                            text = "Needs Work",
                            backgroundColor = Color(0xFFFEF3C7),
                            textColor = Color(0xFFD97706)
                        )
                    }

                    if (percentage >= 100 && !dayData.isBestDay && !dayData.isToday) {
                        ContextBadge(
                            text = "Goal Met",
                            backgroundColor = Color(0xFFDCFCE7),
                            textColor = Color(0xFF16A34A)
                        )
                    } else if (percentage < 50 && !dayData.isWorstDay && dayData.totalMl > 0) {
                        ContextBadge(
                            text = "Low",
                            backgroundColor = Color(0xFFFEE2E2),
                            textColor = Color(0xFFDC2626)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Water consumed
                    Column {
                        Text(
                            text = "Consumed",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatLiters(liters),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Goal
                    Column {
                        Text(
                            text = "Goal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatLiters(goalLiters),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Percentage
                    Column {
                        Text(
                            text = "Achievement",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                percentage >= 100 -> Color(0xFF16A34A)
                                percentage >= 70 -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ContextBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

private fun formatLiters(liters: Float): String {
    return if (liters >= 10) {
        "${liters.toInt()}L"
    } else {
        val rounded = (liters * 10).roundToInt() / 10f
        val intPart = rounded.toInt()
        val decPart = ((rounded - intPart) * 10).roundToInt()
        if (decPart == 0) "${intPart}L" else "$intPart.${decPart}L"
    }
}

@Composable
private fun DayBar(
    data: DayChartData,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    maxMlInWeek: Int = 3000 // Will be passed from parent
) {
    val maxHeight = 160.dp
    // Calculate bar height based on absolute water intake, not percentage of goal
    // This ensures taller bars = more water consumed (matches user intuition)
    val barHeight = if (maxMlInWeek > 0) {
        (maxHeight.value * (data.totalMl.toFloat() / maxMlInWeek.toFloat())).dp.coerceAtMost(maxHeight)
    } else {
        0.dp
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable(
            enabled = !data.isFuture,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(maxHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Bar background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(
                        when {
                            data.isFuture -> MaterialTheme.colorScheme.surfaceVariant
                            isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                            data.progress < 0.3f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            data.goalReached -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
            ) {
                // Highlight on top for high bars or selected
                if ((data.progress >= 0.7f || isSelected) && !data.isFuture) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.White.copy(alpha = if (isSelected) 0.5f else 0.3f))
                    )
                }
            }
        }

        // Day label
        Text(
            text = data.dayLabel,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (data.isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
            color = when {
                isSelected -> MaterialTheme.colorScheme.primary
                data.isToday -> MaterialTheme.colorScheme.primary
                data.isFuture -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun StatsGrid(
    dailyAverage: String,
    dailyAverageChange: String?,
    allTimeTotal: String,
    allTimeSince: String,
    bestDay: String,
    bestDayAmount: String,
    completionRate: Int,
    daysMetGoal: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Outlined.WaterDrop,
                label = "Daily Avg",
                value = dailyAverage,
                subValue = dailyAverageChange,
                subValueColor = if (dailyAverageChange?.startsWith("+") == true)
                    Color(0xFF16A34A) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                icon = Icons.Outlined.Functions,
                label = "Total",
                value = allTimeTotal,
                subValue = allTimeSince,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                icon = Icons.Outlined.EmojiEvents,
                label = "Best Day",
                value = bestDay,
                subValue = bestDayAmount,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                icon = Icons.Outlined.CheckCircle,
                label = "Completion",
                value = "$completionRate%",
                subValue = daysMetGoal,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    subValue: String?,
    modifier: Modifier = Modifier,
    subValueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Surface(
        modifier = modifier.shadowSm(shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            subValue?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (it.startsWith("+") || it.startsWith("-")) FontWeight.Bold else FontWeight.Medium,
                    color = subValueColor
                )
            }
        }
    }
}

@Composable
private fun SmartTipCard(tip: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEFF6FF),
        border = BorderStroke(1.dp, Color(0xFFDBEAFE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Lightbulb,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Smart Tip",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = tip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
