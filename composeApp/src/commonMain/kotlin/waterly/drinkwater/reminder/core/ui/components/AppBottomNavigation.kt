package waterly.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Navigation Tab enum
 * Represents the three main sections of the app
 */
enum class AppNavigationTab {
    HOME,
    PROGRESS,
    SETTINGS
}

/**
 * Centralized Bottom Navigation Bar
 * 
 * Single source of truth for bottom navigation across all screens
 * Follows SOLID principles and DRY
 * 
 * @param currentTab Currently selected tab
 * @param onNavigateToHome Callback for home navigation
 * @param onNavigateToProgress Callback for progress navigation
 * @param onNavigateToSettings Callback for settings navigation
 */
@Composable
fun AppBottomNavigation(
    currentTab: AppNavigationTab,
    onNavigateToHome: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppBottomNavItem(
                icon = Icons.Filled.Home,
                label = "Home",
                isSelected = currentTab == AppNavigationTab.HOME,
                onClick = onNavigateToHome
            )
            
            AppBottomNavItem(
                icon = Icons.Filled.BarChart,
                label = "Progress",
                isSelected = currentTab == AppNavigationTab.PROGRESS,
                onClick = onNavigateToProgress
            )
            
            AppBottomNavItem(
                icon = Icons.Filled.Settings,
                label = "Settings",
                isSelected = currentTab == AppNavigationTab.SETTINGS,
                onClick = onNavigateToSettings
            )
        }
    }
}

/**
 * Bottom Navigation Item
 * 
 * Individual tab in the bottom navigation bar
 * Private to this file - implementation detail
 */
@Composable
private fun AppBottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 4.dp),
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
            fontSize = 11.sp
        )
    }
}
