package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.PerceptionState
import com.example.model.ThreatSeverity
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonGreen

/**
 * Tactical Cyber Radar rendering player position, safe heading,
 * and detected enemy clock positions.
 */
@Composable
fun TacticalRadarView(
    perception: PerceptionState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "RadarSweep")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000),
            repeatMode = RepeatMode.Restart
        ),
        label = "SweepAngle"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = modifier
            .size(240.dp)
            .testTag("tactical_radar_view"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f * 0.88f

            // Radar background
            drawCircle(
                color = Color(0xFF0F1828),
                radius = radius,
                center = center
            )

            // Concentric range circles
            val rings = listOf(0.33f, 0.66f, 1.0f)
            for (fraction in rings) {
                drawCircle(
                    color = CyberCyan.copy(alpha = 0.25f),
                    radius = radius * fraction,
                    center = center,
                    style = Stroke(width = 2f)
                )
            }

            // Crosshairs
            drawLine(
                color = CyberCyan.copy(alpha = 0.2f),
                start = Offset(center.x - radius, center.y),
                end = Offset(center.x + radius, center.y),
                strokeWidth = 2f
            )
            drawLine(
                color = CyberCyan.copy(alpha = 0.2f),
                start = Offset(center.x, center.y - radius),
                end = Offset(center.x, center.y + radius),
                strokeWidth = 2f
            )

            // Rotating sweep line
            val sweepRad = Math.toRadians(sweepAngle.toDouble())
            val sweepEnd = Offset(
                x = (center.x + radius * Math.cos(sweepRad)).toFloat(),
                y = (center.y + radius * Math.sin(sweepRad)).toFloat()
            )
            drawLine(
                color = CyberCyan.copy(alpha = 0.4f),
                start = center,
                end = sweepEnd,
                strokeWidth = 2.5f
            )

            // Safe heading arrow
            val safeRad = Math.toRadians((perception.recommendedHeadingDegrees - 90).toDouble())
            val safeIndicator = Offset(
                x = (center.x + radius * 0.9f * Math.cos(safeRad)).toFloat(),
                y = (center.y + radius * 0.9f * Math.sin(safeRad)).toFloat()
            )
            drawCircle(
                color = NeonGreen,
                radius = 8f,
                center = safeIndicator
            )
            drawLine(
                color = NeonGreen,
                start = center,
                end = safeIndicator,
                strokeWidth = 3f
            )

            // Threat indicator
            if (perception.threatDetected != ThreatSeverity.NONE) {
                val threatAngleRad = Math.toRadians((perception.threatClockDirection * 30 - 90).toDouble())
                val threatDistanceRatio = when (perception.threatDetected) {
                    ThreatSeverity.CRITICAL -> 0.45f
                    ThreatSeverity.MEDIUM -> 0.70f
                    else -> 0.88f
                }
                val threatPos = Offset(
                    x = (center.x + radius * threatDistanceRatio * Math.cos(threatAngleRad)).toFloat(),
                    y = (center.y + radius * threatDistanceRatio * Math.sin(threatAngleRad)).toFloat()
                )

                // Pulsing red danger indicator
                drawCircle(
                    color = AlertRed.copy(alpha = pulseAlpha),
                    radius = 14f,
                    center = threatPos
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = threatPos
                )
            }

            // Center Player Marker
            drawCircle(
                color = HyperViolet,
                radius = 7f,
                center = center
            )
        }
    }
}
