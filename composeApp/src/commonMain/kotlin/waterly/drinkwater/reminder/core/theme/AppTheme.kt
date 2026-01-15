package waterly.drinkwater.reminder.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * HydroTracker App Theme - LIGHT MODE ONLY
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography(),
        content = content
    )
}