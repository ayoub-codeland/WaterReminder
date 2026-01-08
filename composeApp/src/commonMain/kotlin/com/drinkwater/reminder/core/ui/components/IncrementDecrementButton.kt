package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.ui.extensions.shadowSm

/**
 * Reusable Increment/Decrement Button Component
 * 
 * Matches design: rounded-2xl (16dp), white background, soft shadow, border
 * 
 * @param onClick Action to perform when button is clicked
 * @param icon Icon to display (Add or Remove)
 * @param modifier Optional modifier
 * @param size Button size (default 64dp for h-16 w-16)
 * @param enabled Whether button is enabled
 */
@Composable
fun IncrementDecrementButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    enabled: Boolean = true
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(size).shadowSm(RoundedCornerShape(16.dp)),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant, // border-slate-100
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = if (icon == Icons.Default.Add) "Increment" else "Decrement",
                tint = MaterialTheme.colorScheme.onSurface, // text-slate-900
                modifier = Modifier.size(32.dp) // text-[32px]
            )
        }
    }
}

@Composable
fun IncrementButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IncrementDecrementButton(
        onClick = onClick,
        icon = Icons.Default.Add,
        modifier = modifier,
        enabled = enabled
    )
}

@Composable
fun DecrementButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IncrementDecrementButton(
        onClick = onClick,
        icon = Icons.Default.Remove,
        modifier = modifier,
        enabled = enabled
    )
}
