package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.domain.model.AgeGroup
import com.drinkwater.reminder.core.ui.extensions.shadowSm
import com.drinkwater.reminder.features.settings.presentation.profile.toDisplayLabel

/**
 * Common AgeGroup card component for consistent design across app
 * Used in both Profile Setup (onboarding) and Edit Profile (settings)
 */
@Composable
fun AgeGroupCard(
    ageGroup: AgeGroup,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp).shadowSm(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected)
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        else
            BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ageGroup.toDisplayLabel(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
