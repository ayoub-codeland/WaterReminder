package com.drinkwater.reminder.core.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Applies Tailwind shadow-sm effect
 * Matches HTML: shadow-sm (0 1px 2px 0 rgb(0 0 0 / 0.05))
 */
fun Modifier.shadowSm(
    shape: Shape
): Modifier = this.shadow(
    elevation = 1.dp,
    shape = shape,
    spotColor = Color.Black.copy(alpha = 0.3f)
)