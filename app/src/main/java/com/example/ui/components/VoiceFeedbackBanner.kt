package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceFeedbackBanner(
    isListening: Boolean,
    lastCommand: String,
    onMicClick: () -> Unit,
    onQuickCommandSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val micBgColor by animateColorAsState(
        targetValue = if (isListening) AlertRed else CyberCyan,
        label = "MicBgColor"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("voice_feedback_banner")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Voice Assistant",
                    tint = CyberCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "ARABIC VOICE CONTROLLER / التحكم الصوتي",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isListening) "Listening... / جاري الاستماع..." else "Hands-free voice assistant ready",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isListening) AlertRed else NeonGreen
                    )
                }
            }

            // Big 52dp Accessible Mic Button
            BoxAccessibleMic(
                isListening = isListening,
                bgColor = micBgColor,
                onClick = onMicClick
            )
        }

        AnimatedVisibility(visible = lastCommand.isNotBlank()) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = "Last Recognized / آخر أمر منطوق:",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = "“$lastCommand”",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Arabic Accessible Prompts
        Text(
            text = "Accessible Quick Voice Commands / أوامر صوتية سريعة:",
            fontSize = 11.sp,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.height(6.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickCommandChip("ابدأ المساعدة", "Start Assist") { onQuickCommandSelected("ابدأ المساعدة") }
            QuickCommandChip("اشرح لي ما يحدث", "Explain Scene") { onQuickCommandSelected("اشرح لي ما يحدث") }
            QuickCommandChip("طريق الزون", "Zone Route") { onQuickCommandSelected("طريق الزون") }
            QuickCommandChip("اجعل الاستراتيجية دفاعية", "Defensive Mode") { onQuickCommandSelected("اجعل الاستراتيجية دفاعية") }
            QuickCommandChip("توقف فوراً", "Emergency Stop", isAlert = true) { onQuickCommandSelected("توقف فوراً") }
        }
    }
}

@Composable
private fun BoxAccessibleMic(
    isListening: Boolean,
    bgColor: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(bgColor)
            .testTag("accessible_mic_button")
    ) {
        Icon(
            imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicOff,
            contentDescription = if (isListening) "Stop voice listening" else "Start voice listening",
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun QuickCommandChip(
    textAr: String,
    textEn: String,
    isAlert: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isAlert) AlertRed.copy(alpha = 0.2f) else SurfaceElevated)
            .border(
                1.dp,
                if (isAlert) AlertRed else CyberCyan.copy(alpha = 0.4f),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("quick_cmd_${textAr.hashCode()}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = textAr,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isAlert) AlertRed else Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "($textEn)",
            fontSize = 10.sp,
            color = Color.LightGray
        )
    }
}
