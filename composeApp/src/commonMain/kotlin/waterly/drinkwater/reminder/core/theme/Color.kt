package waterly.drinkwater.reminder.core.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme

val Primary = Color(0xFF259df4)
val PrimaryDark = Color(0xFF1a85d6)
val BackgroundLight = Color(0xFFf5f7f8)
val SurfaceLight = Color(0xFFffffff)
val SurfaceVariantLight = Color(0xFFf1f5f9)
val SubtleBorder = Color(0xFF94a3b8)
val SubtleBorderDark = Color(0xFFe2e8f0)
val TextMainLight = Color(0xFF0f172a)
val TextSecondary = Color(0xFF64748b)
val TextTertiary = Color(0xFF475569)  // After TextSecondary


val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Primary.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryDark,

    secondary = TextTertiary,
    onSecondaryContainer = TextTertiary,

    background = BackgroundLight,
    onBackground = TextMainLight,

    surface = SurfaceLight,
    onSurface = TextMainLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,

    outline = SubtleBorder,
    outlineVariant = SubtleBorderDark,

    error = Color(0xFFef4444)
)