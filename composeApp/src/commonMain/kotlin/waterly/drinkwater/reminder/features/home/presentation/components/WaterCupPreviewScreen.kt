import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import waterly.drinkwater.reminder.features.home.presentation.components.WaterCupComponent

/**
 * Demo Preview Screen for Water Cup Component
 *
 * This is a standalone preview/test screen that demonstrates the WaterCupComponent.
 * Use this to test the component in isolation before integrating into your app.
 *
 * Features demonstrated:
 * - Water level animation on amount change
 * - Idle water wave animation
 * - Test popup for adding water
 * - Progress indicator
 */
@Composable
fun WaterCupPreviewScreen() {
    var currentWaterAmount by remember { mutableStateOf(1250) }
    val goalAmount = 2500

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FBFF),
                        Color(0xFFEDF5FC),
                        Color(0xFFE5F0FA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Goal indicator
            Text(
                text = "Goal: ${formatDisplayAmount(goalAmount)} ml",
                fontSize = 16.sp,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Water Cup Component - tap to open popup
            WaterCupComponent(
                currentAmount = currentWaterAmount,
                goalAmount = goalAmount,
                cupWidth = 240.dp,
                cupHeight = 300.dp,
                showTestPopup = true,
                onAmountChanged = { newAmount ->
                    currentWaterAmount = newAmount.coerceIn(0, goalAmount * 2)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Progress percentage
            val progressPercent = (currentWaterAmount.toFloat() / goalAmount * 100).toInt()
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE3F2FD)
            ) {
                Text(
                    text = "$progressPercent% Complete",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF0088CC),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Quick test buttons
            Text(
                text = "Quick Test Buttons",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickTestButton(
                    text = "+250ml",
                    onClick = {
                        currentWaterAmount = (currentWaterAmount + 250).coerceAtMost(goalAmount * 2)
                    }
                )

                QuickTestButton(
                    text = "+500ml",
                    onClick = {
                        currentWaterAmount = (currentWaterAmount + 500).coerceAtMost(goalAmount * 2)
                    }
                )

                QuickTestButton(
                    text = "Reset",
                    onClick = { currentWaterAmount = 0 },
                    isDestructive = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Slider for fine control
            Text(
                text = "Fine Control",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = currentWaterAmount.toFloat(),
                onValueChange = { currentWaterAmount = it.toInt() },
                valueRange = 0f..(goalAmount * 1.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF0088CC),
                    activeTrackColor = Color(0xFF0088CC),
                    inactiveTrackColor = Color(0xFFE0E0E0)
                )
            )

            Text(
                text = "${formatDisplayAmount(currentWaterAmount)} ml",
                fontSize = 14.sp,
                color = Color(0xFF4B5563)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Info text
            Text(
                text = "Tap the cup to open the water addition popup",
                fontSize = 12.sp,
                color = Color(0xFFAAAAAA)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun QuickTestButton(
    text: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDestructive) Color(0xFFFFEBEE) else Color(0xFFE3F2FD),
            contentColor = if (isDestructive) Color(0xFFD32F2F) else Color(0xFF0088CC)
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatDisplayAmount(amount: Int): String {
    return if (amount >= 1000) {
        val thousands = amount / 1000
        val remainder = amount % 1000
        if (remainder == 0) {
            "$thousands,000"
        } else {
            String.format("%d,%03d", thousands, remainder)
        }
    } else {
        amount.toString()
    }
}