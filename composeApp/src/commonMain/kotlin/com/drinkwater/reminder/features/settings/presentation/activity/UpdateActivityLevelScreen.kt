package com.drinkwater.reminder.features.settings.presentation.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.drinkwater.reminder.core.domain.model.ActivityLevel
import com.drinkwater.reminder.core.ui.components.ActivitySlider
import com.drinkwater.reminder.core.ui.components.AppScaffold
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateActivityLevelScreen(
    viewModel: UpdateActivityLevelViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UpdateActivityLevelUiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    UpdateActivityLevelScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun UpdateActivityLevelScreenContent(
    state: UpdateActivityLevelUiState,
    onEvent: (UpdateActivityLevelUiEvent) -> Unit
) {
    AppScaffold(
        topBar = {
            Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.background) {
                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                    IconButton(
                        onClick = { onEvent(UpdateActivityLevelUiEvent.OnBackClick) },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                    
                    Text(
                        "Update Activity Level",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { _ ->
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.5f))
            
            Box(Modifier.padding(bottom = 32.dp), Alignment.Center) {
                Box(
                    Modifier.size(144.dp).background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        CircleShape
                    )
                )
                
                Surface(
                    Modifier.size(128.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    shadowElevation = 8.dp
                ) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Icon(
                            Icons.Default.DirectionsRun,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
                
                Surface(
                    Modifier.align(Alignment.BottomCenter).offset(y = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.WaterDrop, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(14.dp))
                        Text(
                            "+${state.additionalWater}ml",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Text(
                state.displayName,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Text(
                state.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp).padding(bottom = 32.dp)
            )
            
            Column(Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 4.dp).padding(bottom = 16.dp),
                    Arrangement.SpaceBetween
                ) {
                    Text("SEDENTARY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("EXTREMELY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                
                ActivitySlider(
                    value = state.sliderValue,
                    onValueChange = { value ->
                        val level = when (value.toInt()) {
                            1 -> ActivityLevel.SEDENTARY
                            2 -> ActivityLevel.LIGHT
                            3 -> ActivityLevel.MODERATE
                            4 -> ActivityLevel.ACTIVE
                            else -> ActivityLevel.VERY_ACTIVE
                        }
                        onEvent(UpdateActivityLevelUiEvent.OnActivityLevelChanged(level))
                    },
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Surface(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                shadowElevation = 1.dp
            ) {
                Row(Modifier.padding(16.dp), Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Text(
                        "Increasing your activity level will adjust your daily hydration goal. Based on \"${state.displayName}\", we recommend adding ${state.additionalWater}ml to your daily intake.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(Modifier.weight(1f))
            
            Column(Modifier.fillMaxWidth().padding(top = 24.dp)) {
                Button(
                    onClick = { onEvent(UpdateActivityLevelUiEvent.OnSaveClick) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Save Changes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                TextButton(
                    onClick = { onEvent(UpdateActivityLevelUiEvent.OnCancelClick) },
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Text("Cancel", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
