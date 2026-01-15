package waterly.drinkwater.reminder.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import waterly.drinkwater.reminder.core.presentation.UiEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable that handles one-shot side effects from ViewModels
 * 
 * Collects effects from the provided flow and invokes the callback for each effect.
 * This is used to handle navigation, showing toasts, and other one-shot events.
 */
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
