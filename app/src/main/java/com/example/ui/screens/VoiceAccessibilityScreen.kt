package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepVoid
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.viewmodel.AutopilotUiState

@Composable
fun VoiceAccessibilityScreen(
    uiState: AutopilotUiState,
    onToggleSpeechLanguage: () -> Unit,
    onToggleSpeechFeedback: () -> Unit,
    onToggleHighContrast: () -> Unit,
    onSpeechRateChanged: (Float) -> Unit,
    onTestCommandClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepVoid)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("voice_accessibility_screen")
    ) {
        // Accessibility Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessibilityNew,
                        contentDescription = "Accessibility and Voice Settings",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "VOICE & ACCESSIBILITY / إمكانية الوصول والأوامر الصوتية",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Engineered for gamers with motor limitations or missing limbs. Full hands-free voice control in Arabic and English with high-contrast UI.",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Speech Preferences Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "VOICE SPEECH SETTINGS / إعدادات النطق الصوتي",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Language Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Speech Language / لغة المساعد",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (uiState.settings.speechLanguageIsArabic) "العربية (Modern Arabic)" else "English",
                            fontSize = 12.sp,
                            color = CyberCyan
                        )
                    }
                    Switch(
                        checked = uiState.settings.speechLanguageIsArabic,
                        onCheckedChange = { onToggleSpeechLanguage() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = CyberCyan
                        ),
                        modifier = Modifier.testTag("switch_speech_language")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Spoken Feedback Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Audio Narration / النطق الصوتي التلقائي",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Speaks tactical alerts & safe directions",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = uiState.settings.speechFeedbackEnabled,
                        onCheckedChange = { onToggleSpeechFeedback() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonGreen
                        ),
                        modifier = Modifier.testTag("switch_speech_feedback")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Speech Speed Slider
                Text(
                    text = "Voice Speed Rate / سرعة النطق: ${String.format("%.2f", uiState.settings.speechSpeechRate)}x",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Slider(
                    value = uiState.settings.speechSpeechRate,
                    onValueChange = onSpeechRateChanged,
                    valueRange = 0.7f..1.6f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = CyberCyan,
                        activeTrackColor = CyberCyan
                    ),
                    modifier = Modifier.testTag("slider_speech_rate")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual & Haptic Accessibility
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "VISUAL & HAPTIC ACCESSIBILITY / التباين والاهتزاز",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "High Contrast Mode / نمط التباين العالي",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "High visibility borders for low vision",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = uiState.settings.highContrastMode,
                        onCheckedChange = { onToggleHighContrast() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = CyberCyan
                        ),
                        modifier = Modifier.testTag("switch_high_contrast")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Arabic Voice Command Library & Interactive Trainer
        Text(
            text = "Arabic Command Library (Tap to test) / دليل الأوامر الصوتية:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        val sampleCommands = listOf(
            Triple("ابدأ المساعدة", "Start Assist", "يبدأ المساعد الصوتي في تحليل الشاشة وتقديم التوجيه"),
            Triple("اشرح لي ما يحدث", "Explain Scene", "يشرح نسبة الصحة، الساتر المقترح، وموقع الأعداء"),
            Triple("ساعدني في اختيار الطريق", "Nav to Safe Zone", "يوجهك صوتياً نحو المنطقة الآمنة والمسار الخالي"),
            Triple("اجعل الاستراتيجية دفاعية", "Defensive Mode", "يحول التكتيك فوراً لتجنب الاشتباك والاحتماء"),
            Triple("ما حالة الاتصال؟", "Check Diagnostics", "يعطيك تقريراً صوتياً عن الاتصال وشيزوكو والخدمة"),
            Triple("توقف فوراً", "Emergency Stop", "إيقاف طارئ فوري لجميع العمليات وإشعار هزاز")
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sampleCommands.forEach { (cmdAr, cmdEn, descAr) ->
                val isEmergency = cmdAr == "توقف فوراً"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isEmergency) AlertRed.copy(alpha = 0.15f) else SurfaceDark)
                        .border(
                            1.dp,
                            if (isEmergency) AlertRed else SurfaceElevated,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onTestCommandClick(cmdAr) }
                        .padding(14.dp)
                        .testTag("cmd_item_${cmdAr.hashCode()}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "“$cmdAr”",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isEmergency) AlertRed else CyberCyan
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "($cmdEn)",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = descAr,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Test Command",
                        tint = if (isEmergency) AlertRed else CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
