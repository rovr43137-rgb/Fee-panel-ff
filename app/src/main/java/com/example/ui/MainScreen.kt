package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AutopilotOverlayService
import com.example.ui.screens.AutopilotHubScreen
import com.example.ui.screens.PerceptionScreen
import com.example.ui.screens.SystemDiagnosticsScreen
import com.example.ui.screens.TacticalConfigScreen
import com.example.ui.screens.VoiceAccessibilityScreen
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DeepVoid
import com.example.ui.theme.HyperViolet
import com.example.ui.theme.SurfaceDark
import com.example.viewmodel.AutopilotViewModel
import kotlinx.coroutines.launch

enum class NexusNavTab(val titleEn: String, val titleAr: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HUB("Hub", "الرئيسية", Icons.Default.Dashboard),
    RADAR("Radar", "الرادار", Icons.Default.Visibility),
    VOICE("Voice", "الصوت", Icons.Default.Accessibility),
    TACTICS("Tactics", "التكتيك", Icons.Default.Psychology),
    SYSTEM("System", "النظام", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AutopilotViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentTab by remember { mutableStateOf(NexusNavTab.HUB) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Handle back button: if on a secondary tab, return to HUB
    BackHandler(enabled = currentTab != NexusNavTab.HUB) {
        currentTab = NexusNavTab.HUB
    }

    LaunchedEffect(uiState.feedbackMessage) {
        if (uiState.feedbackMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(uiState.feedbackMessage)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "⚡ NEXUS AI AUTOPILOT",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberCyan
                    )
                },
                actions = {
                    // Floating Overlay Launcher Button
                    IconButton(
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                                context.startActivity(intent)
                            } else {
                                AutopilotOverlayService.startService(context)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Floating overlay activated / تم تفعيل النافذة العائمة")
                                }
                            }
                        },
                        modifier = Modifier.testTag("btn_launch_overlay")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureInPicture,
                            contentDescription = "Toggle Floating Overlay",
                            tint = CyberCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NexusNavTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = "${tab.titleEn} (${tab.titleAr})"
                            )
                        },
                        label = {
                            Text(
                                text = "${tab.titleEn}\n${tab.titleAr}",
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                lineHeight = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CyberCyan,
                            selectedTextColor = CyberCyan,
                            indicatorColor = HyperViolet.copy(alpha = 0.3f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DeepVoid)
        ) {
            when (currentTab) {
                NexusNavTab.HUB -> AutopilotHubScreen(
                    uiState = uiState,
                    onStartClick = { viewModel.startAutopilot() },
                    onStopClick = { viewModel.stopAutopilot() },
                    onEmergencyStopClick = { viewModel.triggerEmergencyStop() },
                    onProfileSelected = { viewModel.selectProfile(it) },
                    onMicToggle = {
                        if (uiState.isVoiceListening) {
                            viewModel.stopVoiceListening()
                        } else {
                            viewModel.startVoiceListening()
                        }
                    },
                    onQuickVoiceCommand = { cmd ->
                        viewModel.pairWirelessAdb("0", "") // no-op trigger
                        // evaluate quick voice command
                        when (cmd) {
                            "ابدأ المساعدة" -> viewModel.startAutopilot()
                            "توقف فوراً" -> viewModel.triggerEmergencyStop()
                            "طريق الزون" -> viewModel.selectProfile(com.example.model.TacticalProfileType.NAVIGATION_GUIDE)
                            "اجعل الاستراتيجية دفاعية" -> viewModel.selectProfile(com.example.model.TacticalProfileType.DEFENSIVE_SURVIVOR)
                            "اشرح لي ما يحدث" -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(uiState.perception.recommendedSafeActionAr)
                                }
                            }
                        }
                    }
                )

                NexusNavTab.RADAR -> PerceptionScreen(
                    uiState = uiState,
                    onScenarioSelected = { viewModel.loadScenario(it) }
                )

                NexusNavTab.VOICE -> VoiceAccessibilityScreen(
                    uiState = uiState,
                    onToggleSpeechLanguage = { viewModel.toggleSpeechLanguage() },
                    onToggleSpeechFeedback = { viewModel.toggleSpeechFeedback() },
                    onToggleHighContrast = { viewModel.toggleHighContrast() },
                    onSpeechRateChanged = { viewModel.setSpeechRate(it) },
                    onTestCommandClick = { cmd ->
                        when (cmd) {
                            "ابدأ المساعدة" -> viewModel.startAutopilot()
                            "توقف فوراً" -> viewModel.triggerEmergencyStop()
                            "ساعدني في اختيار الطريق" -> viewModel.selectProfile(com.example.model.TacticalProfileType.NAVIGATION_GUIDE)
                            "اجعل الاستراتيجية دفاعية" -> viewModel.selectProfile(com.example.model.TacticalProfileType.DEFENSIVE_SURVIVOR)
                            "ما حالة الاتصال؟" -> viewModel.refreshDiagnostics()
                            "اشرح لي ما يحدث" -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(uiState.perception.recommendedSafeActionAr)
                                }
                            }
                        }
                    }
                )

                NexusNavTab.TACTICS -> TacticalConfigScreen(
                    uiState = uiState,
                    onAggressionChanged = { viewModel.updateAggression(it) },
                    onSurvivalPriorityChanged = { viewModel.updateSurvivalPriority(it) }
                )

                NexusNavTab.SYSTEM -> SystemDiagnosticsScreen(
                    uiState = uiState,
                    onRequestShizukuClick = { viewModel.requestShizukuPermission() },
                    onPairWirelessAdbClick = { port, code -> viewModel.pairWirelessAdb(port, code) }
                )
            }
        }
    }
}
