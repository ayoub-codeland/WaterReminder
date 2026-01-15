package waterly.drinkwater.reminder.features.settings.presentation.weight

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import waterly.drinkwater.reminder.core.domain.model.WeightUnit
import waterly.drinkwater.reminder.core.ui.components.AppScaffold
import waterly.drinkwater.reminder.core.ui.components.DecrementButton
import waterly.drinkwater.reminder.core.ui.components.IncrementButton
import kotlin.math.roundToInt

@Composable
fun UpdateWeightScreen(
    viewModel: UpdateWeightViewModel
) {
    val state by viewModel.state.collectAsState()

    UpdateWeightScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun UpdateWeightScreenContent(
    state: UpdateWeightState,
    onEvent: (UpdateWeightEvent) -> Unit
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
                            onClick = { onEvent(UpdateWeightEvent.OnBackClick) },
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
                .padding(horizontal = 16.dp, vertical = 32.dp),
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
                    value = "${state.weight}",
                    onValueChange = { value ->
                        value.toFloatOrNull()?.let {
                            onEvent(UpdateWeightEvent.OnWeightChanged(it))
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
                onUnitSelected = { unit -> onEvent(UpdateWeightEvent.OnWeightUnitChanged(unit)) },
                modifier = Modifier.padding(bottom = 40.dp)
            )
            
            // Slider with labels and activity level theme
            Column(Modifier.fillMaxWidth().padding(bottom = 48.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 4.dp).padding(bottom = 16.dp),
                    Arrangement.SpaceBetween
                ) {
                    Text(
                        "${state.minWeight.toInt()}${if (state.weightUnit == WeightUnit.KG) "kg" else "lbs"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${state.maxWeight.toInt()}${if (state.weightUnit == WeightUnit.KG) "kg" else "lbs"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                WeightSlider(
                    weight = state.weight,
                    minWeight = state.minWeight,
                    maxWeight = state.maxWeight,
                    onWeightChange = { weight -> onEvent(UpdateWeightEvent.OnWeightChanged(weight)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ) {
                DecrementButton(
                    onClick = { onEvent(UpdateWeightEvent.OnDecrementWeight) }
                )
                
                IncrementButton(
                    onClick = { onEvent(UpdateWeightEvent.OnIncrementWeight) }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { onEvent(UpdateWeightEvent.OnSaveClick) },
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
    onWeightChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        val trackWidth = maxWidth

        // Base track (light gray)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        // Filled track (blue, proportional to weight value)
        val normalizedValue = ((weight - minWeight) / (maxWeight - minWeight)).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .fillMaxWidth(normalizedValue)
                .height(6.dp)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Vertical separator bars ("|") at step positions
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(0f, 0.25f, 0.5f, 0.75f, 1f).forEach { position ->
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth(position.coerceAtLeast(0.001f))
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(6.dp)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }
            }
        }

        // Circular indicator (thumb) - white with blue border
        val indicatorOffset = trackWidth * normalizedValue

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .pointerInput(minWeight, maxWeight) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        val x = change.position.x.coerceIn(0f, size.width.toFloat())
                        val normalized = x / size.width.toFloat()
                        // Calculate new weight directly without snapping
                        val newWeight = minWeight + (normalized * (maxWeight - minWeight))
                        // Round to 1 decimal place
                        val roundedWeight = (newWeight * 10).roundToInt() / 10f
                        onWeightChange(roundedWeight)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = indicatorOffset - 10.dp) // Center the indicator
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                        shape = CircleShape
                    )
            )
        }
    }
}
