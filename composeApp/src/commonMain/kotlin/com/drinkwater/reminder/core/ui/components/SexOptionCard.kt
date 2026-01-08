package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinkwater.reminder.core.theme.*

/**
 * Biological Sex option card
 *
 * Uses theme colors from Color.kt (matches HTML design)
 */
@Composable
fun SexOptionCard(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        // Main card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(
                    border = BorderStroke(
                        width = if (selected) 2.dp else 1.dp,
                        color = if (selected) Primary else SubtleBorder
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    color = if (selected) Primary.copy(alpha = 0.05f) else SurfaceLight
                )
                .clickable(onClick = onClick)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = if (selected) Primary else Slate400
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Text
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (selected) Primary else Slate600
                )
            }
        }

        // Check badge (only when selected)
        if (selected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = (-8).dp, y = (-8).dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Primary)
                    .border(
                        border = BorderStroke(2.dp, White),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    modifier = Modifier.size(14.dp),
                    tint = White
                )
            }
        }
    }
}
