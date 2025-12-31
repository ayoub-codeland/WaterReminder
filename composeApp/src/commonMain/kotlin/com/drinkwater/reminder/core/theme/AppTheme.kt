package com.drinkwater.reminder.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Light Color Scheme for Material 3
 */
private val LightColorScheme = lightColorScheme(
    primary = LightColors.Primary,
    onPrimary = LightColors.OnPrimary,
    primaryContainer = LightColors.PrimaryContainer,
    onPrimaryContainer = LightColors.OnPrimaryContainer,
    
    secondary = LightColors.Secondary,
    onSecondary = LightColors.OnSecondary,
    secondaryContainer = LightColors.SecondaryContainer,
    onSecondaryContainer = LightColors.OnSecondaryContainer,
    
    tertiary = LightColors.Tertiary,
    onTertiary = LightColors.OnTertiary,
    tertiaryContainer = LightColors.TertiaryContainer,
    onTertiaryContainer = LightColors.OnTertiaryContainer,
    
    error = LightColors.Error,
    onError = LightColors.OnError,
    errorContainer = LightColors.ErrorContainer,
    onErrorContainer = LightColors.OnErrorContainer,
    
    background = LightColors.Background,
    onBackground = LightColors.OnBackground,
    
    surface = LightColors.Surface,
    onSurface = LightColors.OnSurface,
    surfaceVariant = LightColors.SurfaceVariant,
    onSurfaceVariant = LightColors.OnSurfaceVariant,
    
    outline = LightColors.Outline,
    outlineVariant = LightColors.OutlineVariant,
    
    scrim = LightColors.Scrim
)

/**
 * Dark Color Scheme for Material 3
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkColors.Primary,
    onPrimary = DarkColors.OnPrimary,
    primaryContainer = DarkColors.PrimaryContainer,
    onPrimaryContainer = DarkColors.OnPrimaryContainer,
    
    secondary = DarkColors.Secondary,
    onSecondary = DarkColors.OnSecondary,
    secondaryContainer = DarkColors.SecondaryContainer,
    onSecondaryContainer = DarkColors.OnSecondaryContainer,
    
    tertiary = DarkColors.Tertiary,
    onTertiary = DarkColors.OnTertiary,
    tertiaryContainer = DarkColors.TertiaryContainer,
    onTertiaryContainer = DarkColors.OnTertiaryContainer,
    
    error = DarkColors.Error,
    onError = DarkColors.OnError,
    errorContainer = DarkColors.ErrorContainer,
    onErrorContainer = DarkColors.OnErrorContainer,
    
    background = DarkColors.Background,
    onBackground = DarkColors.OnBackground,
    
    surface = DarkColors.Surface,
    onSurface = DarkColors.OnSurface,
    surfaceVariant = DarkColors.SurfaceVariant,
    onSurfaceVariant = DarkColors.OnSurfaceVariant,
    
    outline = DarkColors.Outline,
    outlineVariant = DarkColors.OutlineVariant,
    
    scrim = DarkColors.Scrim
)

/**
 * Main App Theme
 * 
 * Wraps MaterialTheme with custom color schemes, typography, and shapes.
 * Automatically switches between light and dark themes based on system settings.
 * 
 * @param darkTheme Whether to use dark theme (defaults to system preference)
 * @param content The composable content to be themed
 * 
 * Usage:
 * ```
 * AppTheme {
 *     // Your app content here
 * }
 * ```
 * 
 * Force dark theme:
 * ```
 * AppTheme(darkTheme = true) {
 *     // Your app content here
 * }
 * ```
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
