package com.drinkwater.reminder.features.settings.presentation.goal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinkwater.reminder.core.ui.components.AppScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateDailyGoalScreen(
    viewModel: UpdateDailyGoalViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UpdateDailyGoalUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    UpdateDailyGoalScreenContent(state, viewModel::onEvent)
}

@Composable
private fun UpdateDailyGoalScreenContent(
    state: UpdateDailyGoalUiState,
    onEvent: (UpdateDailyGoalUiEvent) -> Unit
) {
    AppScaffold(
        topBar = {
            Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    IconButton(onClick = { onEvent(UpdateDailyGoalUiEvent.OnBackClick) }) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back")
                    }
                    Text("Update Daily Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(40.dp))
                }
            }
        }
    ) { _ ->
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 24.dp),
            Alignment.CenterHorizontally, Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.weight(0.3f))
            
            Column(Modifier.fillMaxWidth(), Alignment.CenterHorizontally, Arrangement.spacedBy(8.dp)) {
                Row(Alignment.Bottom, Arrangement.Center) {
                    Text(
                        state.currentGoal.toString(),
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "ml",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                    )
                }
                
                Surface(
                    Modifier.padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        Arrangement.spacedBy(6.dp), Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                        Text(
                            "RECOMMENDED",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            Text(
                buildAnnotatedString {
                    append("Based on your weight and activity level, we recommend a daily intake of ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)) {
                        append("${state.recommendedGoal} ml")
                    }
                    append(".")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp).padding(vertical = 32.dp)
            )
            
            Column(Modifier.fillMaxWidth().padding(bottom = 32.dp), Arrangement.spacedBy(32.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally), Alignment.CenterVertically) {
                    FilledTonalIconButton(onClick = { onEvent(UpdateDailyGoalUiEvent.OnDecrementGoal) }, Modifier.size(56.dp)) {
                        Icon(Icons.Default.Remove, "Decrease", Modifier.size(30.dp))
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("ADJUSTMENT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            "+/- ${state.adjustmentStep}ml",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    FilledTonalIconButton(onClick = { onEvent(UpdateDailyGoalUiEvent.OnIncrementGoal) }, Modifier.size(56.dp)) {
                        Icon(Icons.Default.Add, "Increase", Modifier.size(30.dp))
                    }
                }
                
                DailyGoalSlider(state.currentGoal, state.minGoal, state.maxGoal) { goal ->
                    onEvent(UpdateDailyGoalUiEvent.OnGoalChanged(goal))
                }
            }
            
            Spacer(Modifier.weight(0.5f))
            
            Column(Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
                Button(
                    onClick = { onEvent(UpdateDailyGoalUiEvent.OnSaveClick) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Save New Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                TextButton(onClick = { onEvent(UpdateDailyGoalUiEvent.OnCancelClick) }, Modifier.fillMaxWidth().height(40.dp)) {
                    Text("Cancel", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun DailyGoalSlider(currentGoal: Int, minGoal: Int, maxGoal: Int, onGoalChange: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Box(Modifier.fillMaxWidth().height(48.dp), Alignment.Center) {
            Box(
                Modifier.fillMaxWidth().height(6.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(3.dp))
            ) {
                Box(
                    Modifier.fillMaxWidth(((currentGoal - minGoal).toFloat() / (maxGoal - minGoal)))
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(blue = 0.7f),
                                    MaterialTheme.colorScheme.primary
                                )
                            ),
                            RoundedCornerShape(3.dp)
                        )
                )
            }
            
            Slider(
                value = currentGoal.toFloat(),
                onValueChange = { onGoalChange(it.toInt()) },
                valueRange = minGoal.toFloat()..maxGoal.toFloat(),
                steps = ((maxGoal - minGoal) / 50) - 1,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.surface,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
        
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp), Arrangement.SpaceBetween) {
            Text("${minGoal}ml", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${maxGoal}ml", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
