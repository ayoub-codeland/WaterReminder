package com.drinkwater.reminder.features.home.presentation

import androidx.lifecycle.viewModelScope
import com.drinkwater.reminder.core.domain.repository.UserProfileRepository
import com.drinkwater.reminder.core.presentation.BaseViewModel
import com.drinkwater.reminder.features.home.domain.usecase.AddWaterIntakeUseCase
import com.drinkwater.reminder.features.home.domain.usecase.GetCurrentStreakUseCase
import com.drinkwater.reminder.features.home.domain.usecase.GetTodayIntakeUseCase
import com.drinkwater.reminder.features.home.domain.usecase.ResetTodayIntakeUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * ViewModel for Home (Dashboard) Screen
 *
 * Follows Clean Architecture and SOLID principles
 * Implements Unidirectional Data Flow (UDF) pattern
 */
class HomeViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val addWaterIntakeUseCase: AddWaterIntakeUseCase,
    private val getTodayIntakeUseCase: GetTodayIntakeUseCase,
    private val getCurrentStreakUseCase: GetCurrentStreakUseCase,
    private val resetTodayIntakeUseCase: ResetTodayIntakeUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeSideEffect>(
    initialState = HomeState()
) {
    
    init {
        loadUserData()
        observeTodayIntake()
        updateDateAndGreeting()
        loadStreak()
    }
    
    override fun onEvent(event: HomeEvent) {
        when (event) {
            // Water tracking
            is HomeEvent.OnAddWater -> handleAddWater(event.amount)
            HomeEvent.OnAddWaterClick -> showAddWaterDialog()
            HomeEvent.OnDismissAddWaterDialog -> dismissAddWaterDialog()
            is HomeEvent.OnCustomAmountChanged -> handleCustomAmountChanged(event.amount)
            HomeEvent.OnConfirmCustomAmount -> confirmCustomAmount()
            
            // Quick add presets
            HomeEvent.OnAddGlass -> handleAddWater(250)
            HomeEvent.OnAddBottle -> handleAddWater(500)
            
            // Daily tip
            HomeEvent.OnDismissDailyTip -> dismissDailyTip()
            
            // Navigation
            HomeEvent.OnNavigateToProgress -> sendEffect(HomeSideEffect.NavigateToProgress)
            HomeEvent.OnNavigateToSettings -> sendEffect(HomeSideEffect.NavigateToSettings)
            
            // Reset
            HomeEvent.OnResetIntake -> resetIntake()
        }
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            try {
                updateState { copy(isLoading = true) }
                
                val profile = userProfileRepository.getProfile()
                val dailyGoal = userProfileRepository.getDailyGoal() ?: 2500
                
                updateState {
                    copy(
                        userName = profile?.let { "Alex" } ?: "User",
                        dailyGoal = dailyGoal,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { copy(isLoading = false) }
                sendEffect(HomeSideEffect.ShowError("Failed to load user data"))
            }
        }
    }
    
    private fun observeTodayIntake() {
        viewModelScope.launch {
            getTodayIntakeUseCase.asFlow().collectLatest { total ->
                val previousIntake = currentState.currentIntake
                val wasGoalReached = previousIntake >= currentState.dailyGoal
                val isGoalReached = total >= currentState.dailyGoal
                
                updateState { copy(currentIntake = total) }
                
                // Show celebration only when goal is first reached
                if (isGoalReached && !wasGoalReached && previousIntake > 0) {
                    sendEffect(HomeSideEffect.ShowGoalReachedCelebration)
                }
                
                // Update streak after intake changes
                loadStreak()
            }
        }
    }
    
    private fun loadStreak() {
        viewModelScope.launch {
            try {
                val streak = getCurrentStreakUseCase()
                updateState { copy(currentStreak = streak) }
            } catch (e: Exception) {
                // Keep existing streak value on error
            }
        }
    }
    
    private fun updateDateAndGreeting() {
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        
        val dayOfWeek = localDateTime.dayOfWeek.name.lowercase()
            .replaceFirstChar { it.uppercase() }
        val month = localDateTime.month.name.lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)
        val day = localDateTime.dayOfMonth
        
        val greeting = when (localDateTime.hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
        
        updateState {
            copy(
                currentDate = "$dayOfWeek, $month $day",
                greeting = greeting
            )
        }
    }
    
    private fun handleAddWater(amount: Int) {
        if (amount <= 0) return
        
        viewModelScope.launch {
            try {
                addWaterIntakeUseCase(amount)
                
                updateState {
                    copy(
                        showAddWaterDialog = false,
                        customWaterAmount = "",
                        customAmountError = null
                    )
                }
                
                // Send feedback
                sendEffect(HomeSideEffect.ShowWaterAddedFeedback(amount))
                
            } catch (e: Exception) {
                sendEffect(HomeSideEffect.ShowError("Failed to add water: ${e.message}"))
            }
        }
    }
    
    private fun showAddWaterDialog() {
        updateState {
            copy(
                showAddWaterDialog = true,
                customWaterAmount = "",
                customAmountError = null
            )
        }
    }
    
    private fun dismissAddWaterDialog() {
        updateState {
            copy(
                showAddWaterDialog = false,
                customWaterAmount = "",
                customAmountError = null
            )
        }
    }
    
    private fun handleCustomAmountChanged(amount: String) {
        // Only allow digits
        val filtered = amount.filter { it.isDigit() }
        
        updateState {
            copy(
                customWaterAmount = filtered,
                customAmountError = null
            )
        }
    }
    
    private fun confirmCustomAmount() {
        val amount = currentState.customWaterAmount.toIntOrNull()
        
        when {
            amount == null || amount <= 0 -> {
                updateState {
                    copy(customAmountError = "Please enter a valid amount")
                }
            }
            amount > 2000 -> {
                updateState {
                    copy(customAmountError = "Maximum 2000ml per addition")
                }
            }
            else -> {
                handleAddWater(amount)
            }
        }
    }
    
    private fun dismissDailyTip() {
        updateState { copy(showDailyTip = false) }
    }
    
    private fun resetIntake() {
        viewModelScope.launch {
            try {
                resetTodayIntakeUseCase()
                updateState { copy(showAddWaterDialog = false) }
            } catch (e: Exception) {
                sendEffect(HomeSideEffect.ShowError("Failed to reset: ${e.message}"))
            }
        }
    }
}
