package com.drinkwater.reminder.features.onboarding.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.domain.model.AgeGroup
import com.drinkwater.reminder.core.domain.model.WeightUnit
import com.drinkwater.reminder.core.ui.components.ActivitySlider
import com.drinkwater.reminder.core.ui.components.AgeGroupButton
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.core.ui.components.BiologicalSex
import com.drinkwater.reminder.core.ui.components.SexOptionCard
import com.drinkwater.reminder.core.ui.components.WeightInputField
import kotlinx.coroutines.flow.collectLatest

/**
 * Profile Setup Screen with proper system insets handling
 */
@Composable
fun ProfileSetupScreen(
    viewModel: ProfileSetupViewModel,
    onNavigateToDashboard: (dailyGoal: Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileSetupUiEffect.NavigateToDashboard -> {
                    onNavigateToDashboard(effect.dailyGoal)
                }
            }
        }
    }

    AppScaffold(
        topBar = {
            // Sticky header - already respects status bar insets
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    // Step indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "STEP 2 OF 2",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Fixed bottom button with gradient - already respects navigation bar insets
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Button(
                    onClick = {
                        viewModel.onEvent(ProfileSetupUiEvent.OnCalculateGoalClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .height(56.dp),
                    enabled = state.weight.isNotEmpty() && !state.isCalculating,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    if (state.isCalculating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Calculate Goal",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { _ ->
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            // Title
            Text(
                text = "Profile Setup",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = "We use these details to calculate your daily hydration target with precision.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Biological Sex
            Text(
                text = "Biological Sex",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SexOptionCard(
                    label = "Male",
                    icon = Icons.Default.Male,
                    selected = state.biologicalSex == BiologicalSex.MALE,
                    onClick = {
                        viewModel.onEvent(ProfileSetupUiEvent.OnBiologicalSexSelected(BiologicalSex.MALE))
                    },
                    modifier = Modifier.weight(1f)
                )
                SexOptionCard(
                    label = "Female",
                    icon = Icons.Default.Female,
                    selected = state.biologicalSex == BiologicalSex.FEMALE,
                    onClick = {
                        viewModel.onEvent(ProfileSetupUiEvent.OnBiologicalSexSelected(BiologicalSex.FEMALE))
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Age Group
            Text(
                text = "Age Group",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AgeGroupButton(
                        label = "18-30",
                        selected = state.ageGroup == AgeGroup.AGE_18_30,
                        onClick = {
                            viewModel.onEvent(ProfileSetupUiEvent.OnAgeGroupSelected(AgeGroup.AGE_18_30))
                        },
                        modifier = Modifier.weight(1f)
                    )
                    AgeGroupButton(
                        label = "31-50",
                        selected = state.ageGroup == AgeGroup.AGE_31_50,
                        onClick = {
                            viewModel.onEvent(ProfileSetupUiEvent.OnAgeGroupSelected(AgeGroup.AGE_31_50))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AgeGroupButton(
                        label = "51-60",
                        selected = state.ageGroup == AgeGroup.AGE_51_60,
                        onClick = {
                            viewModel.onEvent(ProfileSetupUiEvent.OnAgeGroupSelected(AgeGroup.AGE_51_60))
                        },
                        modifier = Modifier.weight(1f)
                    )
                    AgeGroupButton(
                        label = "60+",
                        selected = state.ageGroup == AgeGroup.AGE_60_PLUS,
                        onClick = {
                            viewModel.onEvent(ProfileSetupUiEvent.OnAgeGroupSelected(AgeGroup.AGE_60_PLUS))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Current Weight
            Text(
                text = "Current Weight",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weight input
                WeightInputField(
                    value = state.weight,
                    onValueChange = {
                        viewModel.onEvent(ProfileSetupUiEvent.OnWeightChanged(it))
                    },
                    modifier = Modifier.weight(1f)
                )

                // Unit toggle
                Surface(
                    modifier = Modifier
                        .width(120.dp)
                        .height(66.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        Surface(
                            onClick = {
                                viewModel.onEvent(ProfileSetupUiEvent.OnWeightUnitChanged(WeightUnit.KG))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            color = if (state.weightUnit == WeightUnit.KG) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = if (state.weightUnit == WeightUnit.KG) 2.dp else 0.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "kg",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (state.weightUnit == WeightUnit.KG) {
                                        MaterialTheme.colorScheme.onBackground
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Surface(
                            onClick = {
                                viewModel.onEvent(ProfileSetupUiEvent.OnWeightUnitChanged(WeightUnit.LBS))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            color = if (state.weightUnit == WeightUnit.LBS) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = if (state.weightUnit == WeightUnit.LBS) 2.dp else 0.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "lbs",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (state.weightUnit == WeightUnit.LBS) {
                                        MaterialTheme.colorScheme.onBackground
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Activity Level header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activity Level",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ) {
                    Text(
                        text = when (state.activityLevel) {
                            ActivityLevel.SEDENTARY -> "SEDENTARY"
                            ActivityLevel.LIGHT -> "LIGHT"
                            ActivityLevel.MODERATE -> "MODERATE"
                            ActivityLevel.ACTIVE -> "ACTIVE"
                            ActivityLevel.VERY_ACTIVE -> "VERY ACTIVE"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (state.activityLevel) {
                    ActivityLevel.SEDENTARY -> "Little to no exercise"
                    ActivityLevel.LIGHT -> "Light exercise 1-2 times a week"
                    ActivityLevel.MODERATE -> "Light exercise 1-3 times a week"
                    ActivityLevel.ACTIVE -> "Moderate exercise 4-5 times a week"
                    ActivityLevel.VERY_ACTIVE -> "Intense exercise 5+ times a week"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            val sliderValue = when (state.activityLevel) {
                ActivityLevel.SEDENTARY -> 1f
                ActivityLevel.LIGHT -> 2f
                ActivityLevel.MODERATE -> 3f
                ActivityLevel.ACTIVE -> 4f
                ActivityLevel.VERY_ACTIVE -> 5f
            }

            ActivitySlider(
                value = sliderValue,
                onValueChange = { value ->
                    val level = when (value.toInt()) {
                        1 -> ActivityLevel.SEDENTARY
                        2 -> ActivityLevel.LIGHT
                        3 -> ActivityLevel.MODERATE
                        4 -> ActivityLevel.ACTIVE
                        else -> ActivityLevel.VERY_ACTIVE
                    }
                    viewModel.onEvent(ProfileSetupUiEvent.OnActivityLevelChanged(level))
                },
                enabled = !state.isCalculating,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SEDENTARY",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "VERY ACTIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}