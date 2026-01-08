package com.drinkwater.reminder.features.progress.presentation

import androidx.compose.foundation.*
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
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.extensions.shadowSm


/**
 * Progress Screen - Analytics and statistics view
 * Matches the HTML reference design
 */
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    AppScaffold(
        topBar = {
            ProgressTopBar(onSettingsClick = { viewModel.onEvent(ProgressEvent.OnNavigateToSettings) })
        },
        bottomBar = {
            ProgressBottomNavigation(
                onHomeClick = { viewModel.onEvent(ProgressEvent.OnNavigateToHome) },
                onProgressClick = { /* Already on Progress */ },
                onSettingsClick = { viewModel.onEvent(ProgressEvent.OnNavigateToSettings) }
            )
        }
    ) { _ ->
        ProgressContent(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressTopBar(onSettingsClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Empty spacer for balance
            Box(modifier = Modifier.size(40.dp))
            
            Text(
                text = "Progress",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProgressContent(state: ProgressState) {
    Column(
        modifier = Modifier
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
                        border = BorderStroke(1.dp, if (isPositive) Color(0xFFBBF7D0) else Color(0xFFFECACA))
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadowSm(shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(20.dp)
        ) {
            // Goal line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 40.dp) // Approximately 70% from bottom
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                
                // Goal label
                Text(
                    text = "GOAL",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(y = (-12).dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 4.dp)
                )
            }
            
            // Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                chartData.forEach { dayData ->
                    DayBar(data = dayData)
                }
            }
        }
    }
}

@Composable
private fun DayBar(data: DayChartData) {
    val maxHeight = 160.dp
    val barHeight = (maxHeight.value * data.progress.coerceAtMost(1f)).dp
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            data.progress < 0.3f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            data.goalReached -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
            ) {
                // Highlight on top for high bars
                if (data.progress >= 0.7f && !data.isFuture) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    )
                }
            }
        }
        
        // Day label
        Text(
            text = data.dayLabel,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (data.isToday) FontWeight.Bold else FontWeight.Medium,
            color = when {
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

// ============================================================================
// Bottom Navigation (Reused from HomeScreen pattern)
// ============================================================================

@Composable
private fun ProgressBottomNavigation(
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
                isSelected = true,
                onClick = onProgressClick
            )
            
            BottomNavItem(
                icon = Icons.Filled.Settings,
                label = "Settings",
                isSelected = false,
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
