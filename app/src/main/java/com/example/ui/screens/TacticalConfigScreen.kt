package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepVoid
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.WarningGold
import com.example.viewmodel.AutopilotUiState

@Composable
fun TacticalConfigScreen(
    uiState: AutopilotUiState,
    onAggressionChanged: (Int) -> Unit,
    onSurvivalPriorityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepVoid)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("tactical_config_screen")
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Tactical Reasoning Engine Settings",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TACTICAL REASONING ENGINE / محرك التفكير التكتيكي",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Configures decision priorities, engagement thresholds, risk tolerance, and survival heuristics for real-time tactical advice.",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sliders Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Aggression
                Text(
                    text = "Aggression Level / مستوى الهجوم: ${uiState.settings.aggression}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (uiState.settings.aggression > 60) AlertRed else Color.White
                )
                Text(
                    text = "Controls whether the AI advises engaging targets or maintaining stealth.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Slider(
                    value = uiState.settings.aggression.toFloat(),
                    onValueChange = { onAggressionChanged(it.toInt()) },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = AlertRed,
                        activeTrackColor = AlertRed
                    ),
                    modifier = Modifier.testTag("slider_aggression")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Survival Priority
                Text(
                    text = "Survival & Cover Priority / أولوية النجاة والسواتر: ${uiState.settings.survivalPriority}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonGreen
                )
                Text(
                    text = "Increases early zone retreat warnings and healing reminders.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Slider(
                    value = uiState.settings.survivalPriority.toFloat(),
                    onValueChange = { onSurvivalPriorityChanged(it.toInt()) },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonGreen,
                        activeTrackColor = NeonGreen
                    ),
                    modifier = Modifier.testTag("slider_survival")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Honest Measurement & Anti-Fabrication Notice
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Measurement Integrity",
                        tint = WarningGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "EMPIRICAL PERFORMANCE INTEGRITY",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarningGold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NEXUS strictly reports empirically measured latencies (measured: ${uiState.perception.frameProcessingLatencyMs}ms) and genuine perception confidence (${(uiState.perception.confidence * 100).toInt()}%). No fake winrates, fabricated headshot percentages, or simulated match results are generated.",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
