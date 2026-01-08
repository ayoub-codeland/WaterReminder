package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinkwater.reminder.core.theme.*
import com.drinkwater.reminder.core.theme.SurfaceLight

/**
 * Age group selection button
 *
 * Uses theme colors from Color.kt (matches HTML design)
 */
@Composable
fun AgeGroupButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                            color = Primary,
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
                    Modifier.background(Primary)
                } else {
                    Modifier
                        .border(
                            border = BorderStroke(1.dp, SubtleBorder),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(SurfaceLight)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            color = if (selected) White else Slate600
        )
    }
}