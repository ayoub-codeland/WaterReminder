package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Common save button component used across all screens
 * Provides consistent design for primary action buttons
 *
 * @param text Button text (default: "Save Changes")
 * @param onClick Click handler
 * @param enabled Whether button is enabled (default: true)
 * @param isLoading Whether button is in loading state (default: false)
 * @param modifier Optional modifier
 */
@Composable
fun AppSaveButton(
    text: String = "Save Changes",
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp,
            disabledElevation = 0.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * Fixed bottom button with gradient fade
 * Used for screens with scrollable content where button stays at bottom
 *
 * @param text Button text
 * @param onClick Click handler
 * @param enabled Whether button is enabled
 * @param isLoading Whether button is in loading state
 */
@Composable
fun AppFixedBottomButton(
    text: String = "Save Changes",
    onClick: () -> Unit,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        AppSaveButton(
            text = text,
            onClick = onClick,
            enabled = enabled,
            isLoading = isLoading
        )
    }
}
