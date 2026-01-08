package com.drinkwater.reminder.features.settings.presentation.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.domain.model.VolumeUnit

/**
 * Toggle component for switching between ML and OZ volume units
 *
 * Follows the exact HTML design from settings screen
 */
@Composable
fun VolumeUnitToggle(
    selectedUnit: VolumeUnit,
    onUnitSelected: (VolumeUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row {
            // ML Option
            Surface(
                onClick = { onUnitSelected(VolumeUnit.ML) },
                color = if (selectedUnit == VolumeUnit.ML)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent,
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
            ) {
                Text(
                    text = "ml",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selectedUnit == VolumeUnit.ML)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            // OZ Option
            Surface(
                onClick = { onUnitSelected(VolumeUnit.OZ) },
                color = if (selectedUnit == VolumeUnit.OZ)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent,
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
            ) {
                Text(
                    text = "oz",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selectedUnit == VolumeUnit.OZ)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}