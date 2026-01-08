package com.drinkwater.reminder.features.onboarding.presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.ui.components.AppScaffold

/**
 * Welcome Screen with proper system insets handling
 *
 * Template showing how to use AppScaffold for screens with bottom CTA
 */
@Composable
fun WelcomeScreen(
    onNavigateToProfileSetup: () -> Unit
) {
    AppScaffold(
        bottomBar = {
            // Bottom CTA buttons - automatically respects navigation bar insets
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary button
                Button(
                    onClick = onNavigateToProfileSetup,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Secondary button (optional)
                TextButton(
                    onClick = { /* Skip or login */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "I already have an account",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) { _ ->
        // Main content - automatically respects status bar insets
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // App logo or illustration
            // Icon(...) or Image(...)

            Spacer(modifier = Modifier.height(40.dp))

            // Hero title
            Text(
                text = "Hydrate Smarter,\nFeel Better",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Track your daily water intake with personalized goals and smart reminders.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Additional content
        }
    }
}