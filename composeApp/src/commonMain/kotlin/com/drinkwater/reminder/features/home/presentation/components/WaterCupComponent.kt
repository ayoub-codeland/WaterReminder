package com.drinkwater.reminder.features.home.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Water Cup Component - Realistic water glass with animations
 * Clean implementation with no visual artifacts
 */
@Composable
fun WaterCupComponent(
    currentAmount: Int,
    goalAmount: Int,
    modifier: Modifier = Modifier,
    cupWidth: Dp = 220.dp,
    cupHeight: Dp = 280.dp,
    onAmountChanged: ((Int) -> Unit)? = null,
    showTestPopup: Boolean = false
) {
    var showPopup by remember { mutableStateOf(false) }
    var previousAmount by remember { mutableStateOf(currentAmount) }
    var splashTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(currentAmount) {
        if (currentAmount > previousAmount) {
            splashTrigger++
        }
        previousAmount = currentAmount
    }

    val targetFillLevel = (currentAmount.toFloat() / goalAmount.toFloat()).coerceIn(0f, 1f)

    // Water level with bounce animation
    val animatedFillLevel by animateFloatAsState(
        targetValue = targetFillLevel,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 70f),
        label = "waterLevel"
    )

    // Splash animation state
    var splashProgress by remember { mutableStateOf(0f) }
    var isSplashing by remember { mutableStateOf(false) }

    LaunchedEffect(splashTrigger) {
        if (splashTrigger > 0) {
            isSplashing = true
            splashProgress = 0f
            val startTime = withFrameNanos { it }
            val duration = 900_000_000L
            while (true) {
                val elapsed = withFrameNanos { it } - startTime
                splashProgress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
                if (splashProgress >= 1f) break
            }
            isSplashing = false
            splashProgress = 0f
        }
    }

    // Continuous wave animations
    val infiniteTransition = rememberInfiniteTransition(label = "waves")

    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave1"
    )

    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave2"
    )

    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave3"
    )

    val breathing by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .width(cupWidth)
                .height(cupHeight)
                .then(
                    if (showTestPopup && onAmountChanged != null) {
                        Modifier.clickable { showPopup = true }
                    } else Modifier
                )
        ) {
            val w = size.width
            val h = size.height

            // Cup dimensions matching the image
            val cupTopWidth = w * 0.82f
            val cupBottomWidth = w * 0.52f
            val cupTopY = h * 0.06f
            val cupBottomY = h * 0.94f
            val centerX = w / 2f
            val thickness = w * 0.018f

            drawCupShadow(centerX, cupBottomY, cupBottomWidth)
            drawGlassBody(centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, thickness)

            if (animatedFillLevel > 0.005f) {
                drawWater(
                    centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth,
                    animatedFillLevel, wave1, wave2, wave3, breathing,
                    splashProgress, thickness
                )
            }

            drawGlassHighlights(centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth)
            drawGlassRim(centerX, cupTopY, cupTopWidth, thickness)
        }

        // Text overlay
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = cupHeight * 0.08f)
        ) {
            Text(
                text = "CURRENT",
                color = Color(0xFF0891B2),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = formatAmount(currentAmount),
                    color = Color(0xFF0E7490),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ml",
                    color = Color(0xFF0E7490),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp, start = 2.dp)
                )
            }
        }
    }

    if (showPopup && onAmountChanged != null) {
        WaterAdditionPopup(
            currentAmount, goalAmount,
            onDismiss = { showPopup = false },
            onAddWater = { onAmountChanged(it); showPopup = false }
        )
    }
}

private fun cupWallX(
    y: Float, centerX: Float, cupTopY: Float, cupBottomY: Float,
    cupTopWidth: Float, cupBottomWidth: Float, rightSide: Boolean, inset: Float = 0f
): Float {
    val t = ((y - cupTopY) / (cupBottomY - cupTopY)).coerceIn(0f, 1f)
    val widthAtY = cupTopWidth + (cupBottomWidth - cupTopWidth) * t
    val half = widthAtY / 2f - inset
    return if (rightSide) centerX + half else centerX - half
}

private fun DrawScope.drawCupShadow(centerX: Float, cupBottomY: Float, cupBottomWidth: Float) {
    val rx = cupBottomWidth * 0.55f
    val ry = cupBottomWidth * 0.055f
    drawOval(
        brush = Brush.radialGradient(
            listOf(Color(0x22000000), Color(0x10000000), Color.Transparent),
            center = Offset(centerX, cupBottomY + ry),
            radius = rx
        ),
        topLeft = Offset(centerX - rx, cupBottomY - ry * 0.3f),
        size = Size(rx * 2f, ry * 3f)
    )
}

private fun DrawScope.drawGlassBody(
    centerX: Float, cupTopY: Float, cupBottomY: Float,
    cupTopWidth: Float, cupBottomWidth: Float, thickness: Float
) {
    // Fill path (closed shape for the glass tint)
    val fillPath = Path().apply {
        moveTo(centerX - cupTopWidth / 2f, cupTopY)
        lineTo(centerX + cupTopWidth / 2f, cupTopY)
        lineTo(centerX + cupBottomWidth / 2f, cupBottomY)
        quadraticTo(centerX, cupBottomY + cupBottomWidth * 0.18f, centerX - cupBottomWidth / 2f, cupBottomY)
        close()
    }

    // Draw the fill (subtle glass tint)
    drawPath(fillPath, Brush.verticalGradient(
        listOf(Color(0x08FFFFFF), Color(0x06F0F8FF), Color(0x0AE8F4FA)),
        startY = cupTopY, endY = cupBottomY
    ))

    // Outline path - NO top line, only left wall, bottom, and right wall
    val outlinePath = Path().apply {
        // Start at top-left, go down the left wall
        moveTo(centerX - cupTopWidth / 2f, cupTopY)
        lineTo(centerX - cupBottomWidth / 2f, cupBottomY)
        // Bottom curve
        quadraticTo(centerX, cupBottomY + cupBottomWidth * 0.18f, centerX + cupBottomWidth / 2f, cupBottomY)
        // Right wall going up
        lineTo(centerX + cupTopWidth / 2f, cupTopY)
        // DO NOT close - this prevents the top line
    }

    // Draw the outline (no top line)
    drawPath(outlinePath, Color(0x30A8C8D8), style = Stroke(thickness))
}

private fun DrawScope.drawWater(
    centerX: Float, cupTopY: Float, cupBottomY: Float,
    cupTopWidth: Float, cupBottomWidth: Float, fillLevel: Float,
    wave1: Float, wave2: Float, wave3: Float, breathing: Float,
    splashProgress: Float, thickness: Float
) {
    val inset = thickness * 3f
    val cupHeight = cupBottomY - cupTopY
    val fillableHeight = cupHeight - inset * 2

    // Water surface Y with breathing and splash
    val baseY = cupBottomY - inset - (fillableHeight * fillLevel)
    val breathAmt = cupHeight * 0.003f * breathing
    val splashAmt = if (splashProgress > 0f) {
        val decay = (1f - splashProgress) * (1f - splashProgress)
        sin(splashProgress * 7f * PI.toFloat()) * cupHeight * 0.02f * decay
    } else 0f
    val waterY = baseY + breathAmt + splashAmt

    // Surface dimensions at water level
    val leftX =
        cupWallX(waterY, centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, false, inset)
    val rightX =
        cupWallX(waterY, centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, true, inset)
    val surfaceWidth = rightX - leftX
    val ellipseRx = surfaceWidth / 2f
    val ellipseRy = surfaceWidth * 0.075f

    // Wave parameters
    val baseAmp = ellipseRy * 0.2f
    val splashAmpBoost = if (splashProgress > 0f) {
        ellipseRy * 0.6f * (1f - splashProgress) * (1f - splashProgress)
    } else 0f
    val waveAmp = baseAmp + splashAmpBoost

    // Bottom of water
    val bottomY = cupBottomY - inset
    val bottomLeftX =
        cupWallX(bottomY, centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, false, inset)
    val bottomRightX =
        cupWallX(bottomY, centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, true, inset)

    // === DRAW WATER BODY ===
    // Simple approach: draw the body with a flat top at waterY, then overlay the ellipse

    val waterBodyPath = Path().apply {
        // Start at top-left of water
        moveTo(leftX, waterY)

        // Top edge (straight across - will be covered by ellipse)
        lineTo(rightX, waterY)

        // Right wall going down
        val steps = 20
        for (i in 1..steps) {
            val t = i.toFloat() / steps
            val y = waterY + (bottomY - waterY) * t
            val x =
                cupWallX(y, centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, true, inset)
            lineTo(x, y)
        }

        // Bottom curve
        quadraticTo(centerX, bottomY + (bottomRightX - bottomLeftX) * 0.14f, bottomLeftX, bottomY)

        // Left wall going up
        for (i in steps - 1 downTo 0) {
            val t = i.toFloat() / steps
            val y = waterY + (bottomY - waterY) * t
            val x =
                cupWallX(y, centerX, cupTopY, cupBottomY, cupTopWidth, cupBottomWidth, false, inset)
            lineTo(x, y)
        }

        close()
    }

    // Water body gradient
    drawPath(
        path = waterBodyPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF7ED4F0),
                Color(0xFF58C5E8),
                Color(0xFF2DB0D8),
                Color(0xFF1994C4),
                Color(0xFF0A7EB2)
            ),
            startY = waterY,
            endY = cupBottomY
        )
    )

    // === DRAW ELLIPTICAL SURFACE ON TOP ===
    // This covers the flat top edge and creates the 3D water surface

    drawWaterSurface(
        centerX = centerX,
        waterY = waterY,
        ellipseRx = ellipseRx,
        ellipseRy = ellipseRy,
        waveAmp = waveAmp,
        wave1 = wave1,
        wave2 = wave2,
        wave3 = wave3,
        splashProgress = splashProgress
    )

    // Water sheen/highlight
    drawWaterSheen(centerX, waterY, surfaceWidth, bottomY, ellipseRy)
}

private fun DrawScope.drawWaterSurface(
    centerX: Float,
    waterY: Float,
    ellipseRx: Float,
    ellipseRy: Float,
    waveAmp: Float,
    wave1: Float,
    wave2: Float,
    wave3: Float,
    splashProgress: Float
) {
    // Generate smooth ellipse with wave distortion
    val numPoints = 120
    val points = mutableListOf<Offset>()

    for (i in 0 until numPoints) {
        val angle = 2f * PI.toFloat() * i / numPoints

        // Base ellipse point
        val baseX = centerX + ellipseRx * cos(angle)
        val baseY = waterY + ellipseRy * sin(angle)

        // Wave distortion - stronger on back (top) of ellipse
        val normalizedAngle = (angle / (2f * PI.toFloat()))
        val waveInfluence = (1f - sin(angle).coerceIn(0f, 1f)) * 0.7f + 0.3f

        val w1 = sin(normalizedAngle * 4f * PI.toFloat() + wave1) * waveAmp * 0.5f
        val w2 = sin(normalizedAngle * 6f * PI.toFloat() + wave2) * waveAmp * 0.3f
        val w3 = sin(normalizedAngle * 8f * PI.toFloat() + wave3) * waveAmp * 0.2f

        val splashRipple = if (splashProgress > 0f) {
            val ripple =
                sin(normalizedAngle * 10f * PI.toFloat() - splashProgress * 15f * PI.toFloat())
            ripple * waveAmp * 0.4f * (1f - splashProgress)
        } else 0f

        val totalWave = (w1 + w2 + w3 + splashRipple) * waveInfluence

        points.add(Offset(baseX, baseY + totalWave))
    }

    // Build smooth closed path
    val surfacePath = Path().apply {
        if (points.isEmpty()) return@apply

        moveTo(points[0].x, points[0].y)

        for (i in 1 until points.size) {
            val p0 = points[i - 1]
            val p1 = points[i]
            // Simple line connection - with enough points it's smooth
            lineTo(p1.x, p1.y)
        }
        // Close back to start
        lineTo(points[0].x, points[0].y)
        close()
    }

    // Solid fill - light cyan matching the image
    drawPath(
        path = surfacePath,
        color = Color(0xFF7ED8F2)
    )

    // Subtle depth gradient overlay
    drawPath(
        path = surfacePath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0x00FFFFFF),
                Color(0x15106080)
            ),
            startY = waterY - ellipseRy,
            endY = waterY + ellipseRy
        )
    )

    // Highlight on surface
    val hlX = centerX - ellipseRx * 0.2f
    val hlY = waterY - ellipseRy * 0.25f
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x40FFFFFF), Color(0x18FFFFFF), Color.Transparent),
            center = Offset(hlX, hlY),
            radius = ellipseRx * 0.35f
        ),
        topLeft = Offset(hlX - ellipseRx * 0.3f, hlY - ellipseRy * 0.4f),
        size = Size(ellipseRx * 0.6f, ellipseRy * 0.7f)
    )

    // Splash ripples
    if (splashProgress > 0f && splashProgress < 0.75f) {
        val alpha = (1f - splashProgress / 0.75f) * 0.35f
        val radius = ellipseRx * 0.25f * (1f + splashProgress * 2.5f)
        drawOval(
            color = Color.White.copy(alpha = alpha),
            topLeft = Offset(centerX - radius, waterY - radius * 0.4f),
            size = Size(radius * 2f, radius * 0.8f),
            style = Stroke(width = 2f + splashProgress * 2f)
        )

        if (splashProgress > 0.15f) {
            val a2 = (1f - (splashProgress - 0.15f) / 0.6f).coerceIn(0f, 1f) * 0.25f
            val r2 = ellipseRx * 0.15f * (1f + (splashProgress - 0.15f) * 3f)
            drawOval(
                color = Color.White.copy(alpha = a2),
                topLeft = Offset(centerX - r2, waterY - r2 * 0.4f),
                size = Size(r2 * 2f, r2 * 0.8f),
                style = Stroke(width = 1.5f)
            )
        }
    }
}

private fun DrawScope.drawWaterSheen(
    centerX: Float, waterY: Float, surfaceWidth: Float, bottomY: Float, ellipseRy: Float
) {
    val sheenPath = Path().apply {
        val left = centerX - surfaceWidth * 0.36f
        val right = centerX - surfaceWidth * 0.26f
        val top = waterY + ellipseRy + 5f
        val bottom = bottomY - 10f

        moveTo(left, top)
        lineTo(right, top)
        lineTo(right - 2f, bottom)
        lineTo(left - 2f, bottom)
        close()
    }

    drawPath(
        path = sheenPath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0x25FFFFFF), Color(0x12FFFFFF), Color(0x06FFFFFF)),
            startY = waterY,
            endY = bottomY
        )
    )
}

private fun DrawScope.drawGlassHighlights(
    centerX: Float, cupTopY: Float, cupBottomY: Float,
    cupTopWidth: Float, cupBottomWidth: Float
) {
    // Left highlight
    val leftPath = Path().apply {
        val topX = centerX - cupTopWidth / 2f + cupTopWidth * 0.035f
        val bottomX = centerX - cupBottomWidth / 2f + cupBottomWidth * 0.055f
        val w = cupTopWidth * 0.05f

        moveTo(topX, cupTopY + 6f)
        lineTo(topX + w, cupTopY + 6f)
        lineTo(bottomX + w * 0.65f, cupBottomY - 10f)
        lineTo(bottomX, cupBottomY - 10f)
        close()
    }
    drawPath(
        leftPath, Brush.verticalGradient(
            listOf(Color(0x50FFFFFF), Color(0x30FFFFFF), Color(0x15FFFFFF)),
            startY = cupTopY, endY = cupBottomY
        )
    )

    // Right highlight
    val rightPath = Path().apply {
        val topX = centerX + cupTopWidth / 2f - cupTopWidth * 0.09f
        val bottomX = centerX + cupBottomWidth / 2f - cupBottomWidth * 0.11f
        val w = cupTopWidth * 0.03f

        moveTo(topX, cupTopY + 10f)
        lineTo(topX + w, cupTopY + 10f)
        lineTo(bottomX + w * 0.6f, cupBottomY - 15f)
        lineTo(bottomX, cupBottomY - 15f)
        close()
    }
    drawPath(
        rightPath, Brush.verticalGradient(
            listOf(Color(0x28FFFFFF), Color(0x15FFFFFF), Color(0x08FFFFFF)),
            startY = cupTopY, endY = cupBottomY
        )
    )
}

private fun DrawScope.drawGlassRim(
    centerX: Float, cupTopY: Float, cupTopWidth: Float, thickness: Float
) {
    val rx = cupTopWidth / 2f
    val ry = cupTopWidth * 0.1f

    drawOval(
        color = Color(0x38A0BCC8),
        topLeft = Offset(centerX - rx, cupTopY -ry),
        size = Size(rx * 2f, ry * 2f),
        style = Stroke(width = thickness * 0.7f)
    )

    drawArc(
        brush = Brush.linearGradient(
            listOf(Color(0x60FFFFFF), Color(0x30FFFFFF), Color.Transparent),
            start = Offset(centerX - rx * 0.5f, cupTopY - ry),
            end = Offset(centerX + rx * 0.5f, cupTopY - ry)
        ),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(centerX - rx + 3f, cupTopY - ry + 1f),
        size = Size(rx * 2f - 6f, ry * 2f - 2f),
        style = Stroke(width = 2f)
    )
}

private fun formatAmount(amount: Int): String {
    return if (amount >= 1000) {
        val thousands = amount / 1000
        val remainder = amount % 1000
        if (remainder == 0) "$thousands,000"
        else String.format("%d,%03d", thousands, remainder)
    } else amount.toString()
}

// =============================================================================
// Popup
// =============================================================================

@Composable
fun WaterAdditionPopup(
    currentAmount: Int,
    goalAmount: Int,
    onDismiss: () -> Unit,
    onAddWater: (Int) -> Unit
) {
    var input by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<Int?>(null) }
    val presets = listOf(100 to "Sip", 250 to "Glass", 500 to "Bottle", 750 to "Large")

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = Color.White, shadowElevation = 12.dp) {
            Column(
                modifier = Modifier.padding(24.dp).widthIn(min = 280.dp, max = 340.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "💧 Add Water",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "${formatAmount(currentAmount)} / ${formatAmount(goalAmount)} ml",
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
                Spacer(Modifier.height(20.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    presets.forEach { (amt, label) ->
                        PresetChip(
                            amt,
                            label,
                            selected == amt,
                            { selected = amt; input = amt.toString() },
                            Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = input,
                    onValueChange = { v ->
                        if (v.isEmpty() || v.all { it.isDigit() }) {
                            input = v; selected = null
                        }
                    },
                    label = { Text("Custom amount (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0891B2),
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )

                Spacer(Modifier.height(22.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel", color = Color(0xFF666666))
                    }
                    Button(
                        onClick = {
                            (input.toIntOrNull() ?: 0).let {
                                if (it > 0) onAddWater(
                                    currentAmount + it
                                )
                            }
                        },
                        enabled = (input.toIntOrNull() ?: 0) > 0,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0891B2))
                    ) { Text("Add", fontWeight = FontWeight.SemiBold) }
                }

                Spacer(Modifier.height(10.dp))
                TextButton(onClick = { onAddWater(0) }) {
                    Text("Reset to 0", color = Color(0xFFE53935), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    amount: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) Color(0xFF0891B2) else Color(0xFFF5F7F8),
        border = if (isSelected) null else BorderStroke(
            1.dp,
            Color(0xFFE8EAEC)
        )
    ) {
        Column(
            Modifier.padding(vertical = 14.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "$amount",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color(0xFF2A2A2A)
            )
            Text(
                label,
                fontSize = 10.sp,
                color = if (isSelected) Color(0xFFD0F0F8) else Color(0xFF888888)
            )
        }
    }
}