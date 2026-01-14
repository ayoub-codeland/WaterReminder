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
        topBar()

        Box(modifier = Modifier.weight(1f)) {
            content(PaddingValues())
        }

        bottomBar()
    }
}

