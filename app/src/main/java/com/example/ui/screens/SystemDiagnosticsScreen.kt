package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.WarningGold
import com.example.viewmodel.AutopilotUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SystemDiagnosticsScreen(
    uiState: AutopilotUiState,
    onRequestShizukuClick: () -> Unit,
    onPairWirelessAdbClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var adbPort by remember { mutableStateOf("37000") }
    var adbCode by remember { mutableStateOf("123456") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepVoid)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("system_diagnostics_screen")
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
                        imageVector = Icons.Default.SettingsInputAntenna,
                        contentDescription = "System Diagnostics",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYSTEM, SHIZUKU & FAIR-PLAY POLICY",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Performs real checks for Shizuku binder, Wireless ADB pairing, permission audits, and competitive fair-play policy compliance.",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fair Play Policy Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, WarningGold.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Fair Play Policy",
                        tint = WarningGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ANTI-CHEAT & FAIR-PLAY COMPLIANCE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = WarningGold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Garena Free Fire actively detects and bans third-party input injection tools. To protect player accounts while offering game access to disabled players, NEXUS defaults to Authorized Tactical Audio Guidance & HUD Narration Mode. Direct input automation is restricted to authorized sandboxes and custom profiles.",
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shizuku Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SHIZUKU API DIAGNOSTICS / فحص شيزوكو",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )

                Spacer(modifier = Modifier.height(12.dp))

                DiagStatusRow(
                    label = "Shizuku Manager Installed",
                    isOk = uiState.diagnostics.isShizukuInstalled
                )
                DiagStatusRow(
                    label = "Shizuku Binder Running (pingBinder)",
                    isOk = uiState.diagnostics.isShizukuRunning
                )
                DiagStatusRow(
                    label = "Shizuku Permission Authorized",
                    isOk = uiState.diagnostics.isShizukuAuthorized
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onRequestShizukuClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_request_shizuku"),
                    colors = ButtonDefaults.buttonColors(containerColor = HyperViolet),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Request / Verify Shizuku Permission", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Wireless ADB Pairing Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "WIRELESS ADB PAIRING / إقران تصحيح الأخطاء اللاسلكي",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = CyberCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Enter the 5-digit port and 6-digit code shown in Developer Options > Wireless Debugging > Pair device with pairing code.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = adbPort,
                        onValueChange = { adbPort = it },
                        label = { Text("Port (1024-65535)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_adb_port"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SurfaceElevated
                        )
                    )

                    OutlinedTextField(
                        value = adbCode,
                        onValueChange = { adbCode = it },
                        label = { Text("Pairing Code") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_adb_code"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = SurfaceElevated
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onPairWirelessAdbClick(adbPort, adbCode) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_pair_adb"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Pair Wireless ADB Connection", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                if (uiState.diagnostics.isWirelessAdbPaired) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ Paired successfully on port ${uiState.diagnostics.wirelessAdbPort}",
                        fontSize = 12.sp,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Live Diagnostic Event Log
        Text(
            text = "Live Execution Event Log / سجل الأحداث المباشر:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (uiState.diagnostics.lastEventLog.isEmpty()) {
                    Text(
                        text = "No events logged yet. Start assist to begin perception loop.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
                    uiState.diagnostics.lastEventLog.forEach { event ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = timeFormat.format(Date(event.timestamp)),
                                fontSize = 11.sp,
                                color = CyberCyan
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${event.messageEn} • ${event.messageAr}",
                                fontSize = 11.sp,
                                color = if (event.isError) AlertRed else Color.LightGray,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagStatusRow(label: String, isOk: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.White)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isOk) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = if (isOk) "OK" else "Missing",
                tint = if (isOk) NeonGreen else AlertRed,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isOk) "PASS / متصل" else "NOT READY / غير متاح",
                fontSize = 12.sp,
                color = if (isOk) NeonGreen else AlertRed,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
