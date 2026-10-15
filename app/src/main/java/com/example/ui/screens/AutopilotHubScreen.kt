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
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.model.DecisionUrgency
import com.example.model.TacticalProfileType
import com.example.ui.components.TacticalHudGauges
import com.example.ui.components.VoiceFeedbackBanner
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepVoid
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.WarningGold
import com.example.viewmodel.AutopilotStatus
import com.example.viewmodel.AutopilotUiState

@Composable
fun AutopilotHubScreen(
    uiState: AutopilotUiState,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onEmergencyStopClick: () -> Unit,
    onProfileSelected: (TacticalProfileType) -> Unit,
    onMicToggle: () -> Unit,
    onQuickVoiceCommand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepVoid)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("autopilot_hub_screen")
    ) {
        // Accessibility Mission Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NEXUS AI AUTOPILOT",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = CyberCyan
                        )
                        Text(
                            text = "Play Beyond Physical Limitations • تجاوز الحدود الجسدية",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                    StatusBadge(status = uiState.status)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Accessible Action Controls (Start / Emergency Stop)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Start / Stop Toggle Button
            val isRunning = uiState.status == AutopilotStatus.ACTIVE_ASSISTING
            Button(
                onClick = { if (isRunning) onStopClick() else onStartClick() },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("autopilot_toggle_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) WarningGold else CyberCyan
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Stop Assist" else "Start Assist",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isRunning) "PAUSE / إيقاف" else "START / تشغيل",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            // Emergency Stop Button
            Button(
                onClick = onEmergencyStopClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("emergency_stop_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Emergency Stop",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "EMERGENCY / طارئ",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tactical HUD Gauges (Health, Armor, Ammo, Zone Timer)
        TacticalHudGauges(perception = uiState.perception)

        Spacer(modifier = Modifier.height(16.dp))

        // Live Spoken Advice Card
        val decision = uiState.decision
        if (decision != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_tactical_decision_card"),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when (decision.urgency) {
                        DecisionUrgency.CRITICAL -> AlertRed
                        DecisionUrgency.WARNING -> WarningGold
                        DecisionUrgency.INFO -> CyberCyan
                    }
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = "Tactical Voice Broadcast",
                                tint = when (decision.urgency) {
                                    DecisionUrgency.CRITICAL -> AlertRed
                                    DecisionUrgency.WARNING -> WarningGold
                                    DecisionUrgency.INFO -> CyberCyan
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LIVE TACTICAL VOICE / التوجيه الصوتي",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Urgency Tag
                        Text(
                            text = decision.urgency.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (decision.urgency) {
                                DecisionUrgency.CRITICAL -> AlertRed
                                DecisionUrgency.WARNING -> WarningGold
                                DecisionUrgency.INFO -> NeonGreen
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Arabic Voice Narration (Highlighted for accessibility)
                    Text(
                        text = "🔊 “${decision.voiceSpeechAr}”",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "EN: “${decision.voiceSpeechEn}”",
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Reasoning: ${decision.rationaleEn}",
                        fontSize = 12.sp,
                        color = CyberCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tactical Profile Switcher
        Text(
            text = "Active Tactical Playstyle / الاستراتيجية التكتيكية:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TacticalProfileType.values().forEach { profile ->
                val isSelected = uiState.settings.profileType == profile
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) HyperViolet.copy(alpha = 0.25f) else SurfaceDark)
                        .border(
                            1.dp,
                            if (isSelected) HyperViolet else SurfaceElevated,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onProfileSelected(profile) }
                        .padding(14.dp)
                        .testTag("profile_item_${profile.id}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${profile.titleEn} • ${profile.titleAr}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) CyberCyan else Color.White
                        )
                        Text(
                            text = profile.descriptionAr,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected Profile",
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Voice Feedback & Quick Commands
        VoiceFeedbackBanner(
            isListening = uiState.isVoiceListening,
            lastCommand = uiState.lastVoiceCommandText,
            onMicClick = onMicToggle,
            onQuickCommandSelected = onQuickVoiceCommand
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Telemetry Footer
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Latency: ${uiState.perception.frameProcessingLatencyMs}ms",
                    fontSize = 11.sp,
                    color = CyberCyan
                )
                Text(
                    text = "Frames: ${uiState.diagnostics.totalFramesAnalyzed}",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
                Text(
                    text = "Confidence: ${(uiState.perception.confidence * 100).toInt()}%",
                    fontSize = 11.sp,
                    color = NeonGreen
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: AutopilotStatus) {
    val (label, color) = when (status) {
        AutopilotStatus.IDLE -> "STANDBY / جاهز" to Color.Gray
        AutopilotStatus.ACTIVE_ASSISTING -> "ASSISTING / نشط" to NeonGreen
        AutopilotStatus.PAUSED -> "PAUSED / متوقف" to WarningGold
        AutopilotStatus.EMERGENCY_STOPPED -> "STOPPED / طارئ" to AlertRed
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}
