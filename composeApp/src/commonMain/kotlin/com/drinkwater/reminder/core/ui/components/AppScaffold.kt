package com.drinkwater.reminder.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * App-wide scaffold that handles system window insets
 *
 * Ensures content respects:
 * - Status bar (top)
 * - Navigation bar (bottom)
 * - System gestures
 *
 * Use this as the root container for all screens
 */
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Top bar (already inside insets padding)
        topBar()

        // Main content
        Box(modifier = Modifier.weight(1f)) {
            content(PaddingValues())
        }

        // Bottom bar (already inside insets padding)
        bottomBar()
    }
}

/**
 * Simple screen container with system insets
 * Use when you don't need separate top/bottom bars
 */
@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        content()
    }
}