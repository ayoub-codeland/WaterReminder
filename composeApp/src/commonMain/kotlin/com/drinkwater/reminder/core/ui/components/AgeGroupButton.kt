package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.theme.BackgroundLight

/**
 * Age group selection button
 *
 * Uses theme typography and colors (NO hardcoded styles)
 *
 * HTML reference:
 * - Selected: text-sm font-bold = titleSmall
 * - Unselected: text-sm font-semibold = labelLarge
 */
@Composable
fun AgeGroupButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .height(56.dp)
            .then(
                if (selected) {
                    // Ring effect with offset
                    Modifier.drawBehind {
                        val ringOffset = 2.dp.toPx()
                        val ringStroke = 2.dp.toPx()

                        // Draw ring offset background
                        drawRoundRect(
                            color = BackgroundLight,
                            topLeft = androidx.compose.ui.geometry.Offset(-ringOffset, -ringOffset),
                            size = androidx.compose.ui.geometry.Size(
                                size.width + (ringOffset * 2),
                                size.height + (ringOffset * 2)
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                (16.dp + ringOffset.toDp()).toPx()
                            )
                        )

                        // Draw ring stroke
                        drawRoundRect(
                            color = primaryColor,
                            topLeft = androidx.compose.ui.geometry.Offset(-ringOffset, -ringOffset),
                            size = androidx.compose.ui.geometry.Size(
                                size.width + (ringOffset * 2),
                                size.height + (ringOffset * 2)
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                (16.dp + ringOffset.toDp()).toPx()
                            ),
                            style = Stroke(width = ringStroke)
                        )
                    }
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (selected) {
                    Modifier.background(MaterialTheme.colorScheme.primary)
                } else {
                    Modifier
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Text - Uses theme typography
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,     // SemiBold (600)
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}