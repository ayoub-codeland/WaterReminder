package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Activity level slider with visual track filling
 *
 * Matches HTML design exactly:
 * - No bullets/dots
 * - Vertical "|" separators at each step
 * - White circular indicator with blue border
 * - Track fills in blue as user drags
 */
@Composable
fun ActivitySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        val trackWidth = maxWidth

        // Base track (light gray)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        // Filled track (blue, proportional to value)
        // Value range is 1-5, so normalize to 0-1 for width calculation
        val normalizedValue = (value - 1f) / (5f - 1f)
        Box(
            modifier = Modifier
                .fillMaxWidth(normalizedValue.coerceIn(0f, 1f))
                .height(6.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Vertical separator bars ("|") at step positions
        // Positions: 0%, 25%, 50%, 75%, 100%
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { position ->
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth(position.coerceAtLeast(0.001f)) // Avoid 0 width
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(6.dp)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(3))
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }
            }
        }

        // Circular indicator (thumb) - white with blue border
        val indicatorOffset = trackWidth * normalizedValue

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .pointerInput(enabled) {
                    if (enabled) {
                        detectHorizontalDragGestures { change, _ ->
                            change.consume()
                            val x = change.position.x.coerceIn(0f, size.width.toFloat())
                            val normalized = x / size.width.toFloat()
                            // Convert to 1-5 range
                            val newValue = (normalized * 4f + 1f).coerceIn(1f, 5f)
                            // Snap to nearest integer
                            val snappedValue = newValue.roundToInt().toFloat()
                            onValueChange(snappedValue)
                        }
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = indicatorOffset - 10.dp) // Center the indicator
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                        shape = CircleShape
                    )
            )
        }
    }
}