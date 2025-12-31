package com.drinkwater.reminder.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Shape System
 * 
 * Defines corner radius for different UI components.
 * 
 * Usage:
 * - extraSmall: Chips, small buttons
 * - small: Text fields, small cards
 * - medium: Cards, dialogs (most common)
 * - large: Bottom sheets, large cards
 * - extraLarge: Hero images, feature cards
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // Minimal rounding
    small = RoundedCornerShape(8.dp),        // Subtle rounding
    medium = RoundedCornerShape(12.dp),      // Standard rounding (default)
    large = RoundedCornerShape(16.dp),       // Prominent rounding
    extraLarge = RoundedCornerShape(28.dp)   // Maximum rounding
)
