package waterly.drinkwater.reminder.features.settings.presentation.goal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import waterly.drinkwater.reminder.core.ui.components.AppScaffold
import waterly.drinkwater.reminder.core.ui.components.DecrementButton
import waterly.drinkwater.reminder.core.ui.components.IncrementButton
import kotlin.math.roundToInt

@Composable
fun UpdateDailyGoalScreen(
    viewModel: UpdateDailyGoalViewModel
) {
    val state by viewModel.state.collectAsState()

    UpdateDailyGoalScreenContent(state, viewModel::onEvent)
}

@Composable
private fun UpdateDailyGoalScreenContent(
    state: UpdateDailyGoalState,
    onEvent: (UpdateDailyGoalEvent) -> Unit
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
                            onClick = { onEvent(UpdateDailyGoalEvent.OnBackClick) },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Text(
                            text = "Update Daily Goal",
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
            // 1. Based on weight message - FIRST
            Text(
                buildAnnotatedString {
                    append("Based on your weight and activity level, we recommend a daily intake of ")
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        append("${state.recommendedGoal} ml")
                    }
                    append(".")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp).padding(bottom = 32.dp)
            )

            // 2. Current goal display
            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    state.currentGoal.toString(),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "ml",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }

            // 3. Recommended badge
            Surface(
                modifier = Modifier.padding(bottom = 40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    Arrangement.spacedBy(6.dp), 
                    Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Verified,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        "RECOMMENDED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 4. Slider with labels (activity theme)
            Column(Modifier.fillMaxWidth().padding(bottom = 48.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 4.dp).padding(bottom = 16.dp),
                    Arrangement.SpaceBetween
                ) {
                    Text(
                        "${state.minGoal}ml",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${state.maxGoal}ml",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DailyGoalSlider(
                    currentGoal = state.currentGoal,
                    minGoal = state.minGoal,
                    maxGoal = state.maxGoal,
                    onGoalChange = { goal ->
                        onEvent(UpdateDailyGoalEvent.OnGoalChanged(goal))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 5. Decrement button - Adjustment label - Increment button (HORIZONTAL ROW)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Decrement button at start
                DecrementButton(
                    onClick = { onEvent(UpdateDailyGoalEvent.OnDecrementGoal) }
                )
                
                // Adjustment label in middle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "ADJUSTMENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "+/- ${state.adjustmentStep}ml",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                // Increment button at end
                IncrementButton(
                    onClick = { onEvent(UpdateDailyGoalEvent.OnIncrementGoal) }
                )
            }

            Spacer(Modifier.weight(1f))

            // 6. Save button
            Button(
                onClick = { onEvent(UpdateDailyGoalEvent.OnSaveClick) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Save",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyGoalSlider(
    currentGoal: Int,
    minGoal: Int,
    maxGoal: Int,
    onGoalChange: (Int) -> Unit,
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

        // Filled track (blue, proportional to goal value)
        val normalizedValue = ((currentGoal - minGoal).toFloat() / (maxGoal - minGoal).toFloat()).coerceIn(0f, 1f)
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
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        val x = change.position.x.coerceIn(0f, size.width.toFloat())
                        val normalized = x / size.width.toFloat()
                        // Calculate new goal directly
                        val newGoal = minGoal + (normalized * (maxGoal - minGoal))
                        // Round to nearest 50ml step (keeps original behavior)
                        val roundedGoal = ((newGoal / 50f).roundToInt() * 50).coerceIn(minGoal, maxGoal)
                        onGoalChange(roundedGoal)
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
