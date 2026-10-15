package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perception.SampleFrameProvider
import com.example.ui.components.TacticalRadarView
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepVoid
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.WarningGold
import com.example.viewmodel.AutopilotUiState

@Composable
fun PerceptionScreen(
    uiState: AutopilotUiState,
    onScenarioSelected: (SampleFrameProvider.Scenario) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepVoid)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("perception_screen")
    ) {
        // Section Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Vision Perception",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "VISION PERCEPTION & RADAR / الرؤية الحاسوبية والرادار",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Analyzes HUD regions (Health Bar, Minimap red threat markers, Zone timer). Live radar translates visual cues into 360° directional audio cues.",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Center Tactical Cyber Radar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, HyperViolet.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "360° TACTICAL RADAR • رادار التهديدات والملاحة",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )
                Spacer(modifier = Modifier.height(12.dp))
                TacticalRadarView(perception = uiState.perception)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    RadarLegend(label = "Safe Heading", color = NeonGreen)
                    RadarLegend(label = "Enemy Threat", color = AlertRed)
                    RadarLegend(label = "Player Center", color = HyperViolet)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scenario Testbench Selector
        Text(
            text = "Game Scenario Testbench / سيناريوهات الاختبار المباشر:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SampleFrameProvider.Scenario.values().forEach { scenario ->
                val isSelected = uiState.activeScenario == scenario
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) CyberCyan.copy(alpha = 0.2f) else SurfaceDark)
                        .border(
                            1.dp,
                            if (isSelected) CyberCyan else SurfaceElevated,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onScenarioSelected(scenario) }
                        .padding(14.dp)
                        .testTag("scenario_chip_${scenario.name}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${scenario.titleEn} • ${scenario.titleAr}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) CyberCyan else Color.White
                        )
                        Text(
                            text = scenario.expectedState.recommendedSafeActionAr,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                    if (isSelected) {
                        Text(
                            text = "ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyberCyan
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Extracted Perceptual Metrics Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "EXTRACTED SCENE TELEMETRY / بيانات الإدراك المستخرجة",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )
                Spacer(modifier = Modifier.height(12.dp))

                PerceptionMetricRow(
                    label = "Health Detection",
                    value = "${uiState.perception.healthPercentage}% HP (Histogram analyzed)"
                )
                PerceptionMetricRow(
                    label = "Threat Status",
                    value = "${uiState.perception.threatDetected.labelEn} at ${uiState.perception.threatClockDirection} o'clock"
                )
                PerceptionMetricRow(
                    label = "Threat Distance",
                    value = if (uiState.perception.threatDistanceMeters > 0) "${uiState.perception.threatDistanceMeters}m" else "None in close range"
                )
                PerceptionMetricRow(
                    label = "Safe Zone Heading",
                    value = "${uiState.perception.recommendedHeadingDegrees.toInt()}° (Countdown: ${uiState.perception.zoneTimerSeconds}s)"
                )
                PerceptionMetricRow(
                    label = "Pipeline Latency",
                    value = "${uiState.perception.frameProcessingLatencyMs} ms (Target < 30ms)"
                )
                PerceptionMetricRow(
                    label = "Model Confidence",
                    value = "${(uiState.perception.confidence * 100).toInt()}% Verified"
                )
            }
        }
    }
}

@Composable
private fun RadarLegend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, color = Color.LightGray)
    }
}

@Composable
private fun PerceptionMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
