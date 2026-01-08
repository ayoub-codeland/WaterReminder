package com.drinkwater.reminder.features.settings.presentation.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinkwater.reminder.core.ui.components.AppScaffold
import com.drinkwater.reminder.features.onboarding.presentation.profile.WeightUnit
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateWeightScreen(
    viewModel: UpdateWeightViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UpdateWeightUiEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is UpdateWeightUiEffect.ShowError -> {
                    // TODO: Show error toast
                }
            }
        }
    }

    UpdateWeightScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun UpdateWeightScreenContent(
    state: UpdateWeightUiState,
    onEvent: (UpdateWeightUiEvent) -> Unit
) {
    AppScaffold(
        topBar = {
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        IconButton(
                            onClick = { onEvent(UpdateWeightUiEvent.OnBackClick) },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Text(
                            text = "Update Weight",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "We use your weight to calculate your personalized daily hydration goal.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp).padding(bottom = 32.dp)
            )
            
            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                BasicTextField(
                    value = String.format("%.1f", state.weight),
                    onValueChange = { value ->
                        value.toFloatOrNull()?.let {
                            onEvent(UpdateWeightUiEvent.OnWeightChanged(it))
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.width(240.dp)
                )
                
                Text(
                    text = if (state.weightUnit == WeightUnit.KG) "kg" else "lbs",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }
            
            WeightUnitToggle(
                selectedUnit = state.weightUnit,
                onUnitSelected = { unit -> onEvent(UpdateWeightUiEvent.OnWeightUnitChanged(unit)) },
                modifier = Modifier.padding(bottom = 40.dp)
            )
            
            WeightSlider(
                weight = state.weight,
                minWeight = state.minWeight,
                maxWeight = state.maxWeight,
                weightUnit = state.weightUnit,
                onWeightChange = { weight -> onEvent(UpdateWeightUiEvent.OnWeightChanged(weight)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).padding(bottom = 48.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ) {
                FilledTonalIconButton(
                    onClick = { onEvent(UpdateWeightUiEvent.OnDecrementWeight) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(32.dp))
                }
                
                FilledTonalIconButton(
                    onClick = { onEvent(UpdateWeightUiEvent.OnIncrementWeight) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(32.dp))
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { onEvent(UpdateWeightUiEvent.OnSaveClick) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Save", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun WeightUnitToggle(
    selectedUnit: WeightUnit,
    onUnitSelected: (WeightUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.width(160.dp).height(40.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(Modifier.fillMaxSize().padding(4.dp)) {
            Surface(
                onClick = { onUnitSelected(WeightUnit.KG) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                color = if (selectedUnit == WeightUnit.KG) MaterialTheme.colorScheme.surface else Color.Transparent,
                shadowElevation = if (selectedUnit == WeightUnit.KG) 1.dp else 0.dp
            ) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(
                        "kg",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selectedUnit == WeightUnit.KG) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Surface(
                onClick = { onUnitSelected(WeightUnit.LBS) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                color = if (selectedUnit == WeightUnit.LBS) MaterialTheme.colorScheme.surface else Color.Transparent,
                shadowElevation = if (selectedUnit == WeightUnit.LBS) 1.dp else 0.dp
            ) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(
                        "lbs",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selectedUnit == WeightUnit.LBS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun WeightSlider(
    weight: Float,
    minWeight: Float,
    maxWeight: Float,
    weightUnit: WeightUnit,
    onWeightChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Box(Modifier.fillMaxWidth().height(48.dp), Alignment.Center) {
            Box(
                Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    Modifier.fillMaxWidth(((weight - minWeight) / (maxWeight - minWeight)).coerceIn(0f, 1f))
                        .fillMaxHeight().clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            
            Slider(
                value = weight,
                onValueChange = onWeightChange,
                valueRange = minWeight..maxWeight,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.surface,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
        
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp), Arrangement.SpaceBetween) {
            Text(
                "${minWeight.toInt()}${if (weightUnit == WeightUnit.KG) "kg" else "lbs"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "${maxWeight.toInt()}${if (weightUnit == WeightUnit.KG) "kg" else "lbs"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
