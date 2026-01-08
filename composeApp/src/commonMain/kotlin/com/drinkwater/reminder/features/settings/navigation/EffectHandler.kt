package com.drinkwater.reminder.features.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.drinkwater.reminder.core.presentation.UiEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T : UiEffect> HandleEffects(
    effectFlow: Flow<T>,
    onEffect: (T) -> Unit
) {
    LaunchedEffect(Unit) {
        effectFlow.collectLatest { effect ->
            onEffect(effect)
        }
    }
}
