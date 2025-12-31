package com.drinkwater.reminder.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing Unidirectional Data Flow (UDF) pattern.
 *
 * Architecture:
 * - STATE: Immutable UI state exposed via StateFlow (survives config changes)
 * - EVENTS: User interactions processed via onEvent() method
 * - EFFECTS: One-shot events sent via Channel (consumed once)
 *
 * Type Parameters:
 * @param S State type implementing UiState
 * @param E Event type implementing UiEvent
 * @param F Effect type implementing UiEffect
 *
 * Lifecycle:
 * - Automatically manages coroutine scope with SupervisorJob
 * - Call onCleared() when ViewModel is no longer needed
 *
 * Example implementation:
 * ```
 * class HomeViewModel : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
 *     initialState = HomeUiState()
 * ) {
 *     override fun onEvent(event: HomeUiEvent) {
 *         when (event) {
 *             is HomeUiEvent.OnRefreshClicked -> handleRefresh()
 *             is HomeUiEvent.OnItemSelected -> handleSelection(event.id)
 *         }
 *     }
 *
 *     private fun handleRefresh() {
 *         updateState { copy(isLoading = true) }
 *         viewModelScope.launch {
 *             // Perform work
 *             updateState { copy(isLoading = false) }
 *         }
 *     }
 * }
 * ```
 */
abstract class BaseViewModel<S : UiState, E : UiEvent, F : UiEffect>(
    initialState: S
) {
    // Mutable state for internal updates
    private val _state = MutableStateFlow(initialState)
    
    /**
     * Immutable state exposed to UI.
     * Collect this in your Composables to observe state changes.
     */
    val state: StateFlow<S> = _state.asStateFlow()

    // Channel for one-shot effects (capacity = unlimited to never lose effects)
    private val _effect = Channel<F>(Channel.UNLIMITED)
    
    /**
     * Flow of one-shot effects.
     * Collect this in your Composables to handle effects like navigation or toasts.
     */
    val effect = _effect.receiveAsFlow()

    /**
     * Coroutine scope tied to ViewModel lifecycle.
     * Uses SupervisorJob to prevent child failure from canceling siblings.
     * Dispatchers.Main.immediate for immediate UI updates.
     */
    protected val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    /**
     * Current state value accessor for convenience.
     */
    protected val currentState: S
        get() = _state.value

    /**
     * Process user events.
     * Override this method to handle screen-specific events.
     *
     * @param event The event to process
     */
    abstract fun onEvent(event: E)

    /**
     * Update state in a thread-safe manner.
     * Uses a reducer function to compute new state from current state.
     *
     * @param reduce Function that receives current state and returns new state
     *
     * Example:
     * ```
     * updateState { copy(counter = counter + 1) }
     * ```
     */
    protected fun updateState(reduce: S.() -> S) {
        _state.value = currentState.reduce()
    }

    /**
     * Send a one-shot effect to the UI.
     * Effects are delivered via Channel and consumed once.
     *
     * @param effect The effect to send
     *
     * Example:
     * ```
     * sendEffect(HomeUiEffect.NavigateToSettings)
     * ```
     */
    protected fun sendEffect(effect: F) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * Clean up resources.
     * Call this method when the ViewModel is no longer needed.
     * Cancels all running coroutines and closes the effect channel.
     */
    open fun onCleared() {
        viewModelScope.cancel()
        _effect.close()
    }
}
