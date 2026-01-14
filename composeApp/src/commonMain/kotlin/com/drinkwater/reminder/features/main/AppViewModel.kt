package com.drinkwater.reminder.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppUiState>(AppUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        viewModelScope.launch {
            // Simulate a splash screen delay if needed, or remove for speed
            // delay(1000)

            val profile = getUserProfileUseCase()
            val destination = if (profile != null) "home_graph" else "onboarding_graph"

            _uiState.value = AppUiState.Ready(startDestination = destination)
        }
    }
}