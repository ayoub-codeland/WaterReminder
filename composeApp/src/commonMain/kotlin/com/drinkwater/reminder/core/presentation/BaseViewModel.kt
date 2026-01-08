package com.drinkwater.reminder.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, E : UiEvent, F : UiEffect>(
    initialState: S
) : ViewModel() {
    
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = Channel<F>(Channel.CONFLATED)
    val effect = _effect.receiveAsFlow()

    protected val currentState: S
        get() = _state.value

    abstract fun onEvent(event: E)

    protected fun updateState(reduce: S.() -> S) {
        _state.value = currentState.reduce()
    }

    protected fun sendEffect(effect: F) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}
