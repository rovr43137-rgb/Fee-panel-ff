package com.example.viewmodel

import android.app.Application
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.input.InputCapabilityManager
import com.example.model.ActiveInputMode
import com.example.model.DiagnosticEvent
import com.example.model.GameProfile
import com.example.model.PerceptionState
import com.example.model.SystemDiagnostics
import com.example.model.TacticalDecision
import com.example.model.TacticalProfileType
import com.example.model.TacticalSettings
import com.example.perception.SampleFrameProvider
import com.example.perception.VisionPipeline
import com.example.service.AutopilotOverlayService
import com.example.tactics.TacticalReasoningEngine
import com.example.voice.ParsedVoiceCommand
import com.example.voice.VoiceAssistantManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AutopilotStatus {
    IDLE,
    ACTIVE_ASSISTING,
    PAUSED,
    EMERGENCY_STOPPED
}

data class AutopilotUiState(
    val status: AutopilotStatus = AutopilotStatus.IDLE,
    val selectedGame: GameProfile = GameProfile.FREE_FIRE,
    val perception: PerceptionState = PerceptionState(),
    val decision: TacticalDecision? = null,
    val settings: TacticalSettings = TacticalSettings(),
    val diagnostics: SystemDiagnostics = SystemDiagnostics(),
    val activeScenario: SampleFrameProvider.Scenario = SampleFrameProvider.Scenario.COMBAT_CRITICAL,
    val isVoiceListening: Boolean = false,
    val lastVoiceCommandText: String = "",
    val feedbackMessage: String = ""
)

class AutopilotViewModel(application: Application) : AndroidViewModel(application) {

    private val visionPipeline = VisionPipeline(GameProfile.FREE_FIRE)
    private val reasoningEngine = TacticalReasoningEngine()
    private val capabilityManager = InputCapabilityManager(application)

    private val _uiState = MutableStateFlow(AutopilotUiState())
    val uiState: StateFlow<AutopilotUiState> = _uiState.asStateFlow()

    private var loopJob: Job? = null
    private var voiceAssistant: VoiceAssistantManager? = null

    init {
        initVoiceAssistant()
        refreshDiagnostics()
        // Initialize with default scenario
        loadScenario(SampleFrameProvider.Scenario.COMBAT_CRITICAL)
    }

    private fun initVoiceAssistant() {
        voiceAssistant = VoiceAssistantManager(
            context = getApplication(),
            onCommandReceived = { command -> handleVoiceCommand(command) },
            onListeningStateChanged = { isListening ->
                _uiState.update { it.copy(isVoiceListening = isListening) }
            }
        )
    }

    fun refreshDiagnostics() {
        val diag = capabilityManager.auditCapabilities(_uiState.value.selectedGame)
        _uiState.update { current ->
            current.copy(
                diagnostics = current.diagnostics.copy(
                    isShizukuInstalled = diag.isShizukuInstalled,
                    isShizukuRunning = diag.isShizukuRunning,
                    isShizukuAuthorized = diag.isShizukuAuthorized,
                    activeInputMode = diag.activeInputMode
                )
            )
        }
    }

    fun startAutopilot() {
        if (_uiState.value.status == AutopilotStatus.ACTIVE_ASSISTING) return

        _uiState.update {
            it.copy(
                status = AutopilotStatus.ACTIVE_ASSISTING,
                feedbackMessage = "NEXUS Assist Active / المساعد نشط"
            )
        }

        // Start closed loop
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            while (isActive && _uiState.value.status == AutopilotStatus.ACTIVE_ASSISTING) {
                runPerceptionCycle()
                delay(_uiState.value.settings.frameAnalysisIntervalMs)
            }
        }

        speakTacticalNotification("تم تفعيل مساعد نيكسوس التكتيكي", "NEXUS Tactical Assist activated")
    }

    fun stopAutopilot() {
        loopJob?.cancel()
        loopJob = null
        _uiState.update {
            it.copy(
                status = AutopilotStatus.IDLE,
                feedbackMessage = "Assist Stopped / تم إيقاف المساعدة"
            )
        }
        speakTacticalNotification("تم إيقاف المساعد", "Assistant stopped")
    }

    fun triggerEmergencyStop() {
        loopJob?.cancel()
        loopJob = null
        vibrateEmergency()
        _uiState.update {
            it.copy(
                status = AutopilotStatus.EMERGENCY_STOPPED,
                diagnostics = it.diagnostics.copy(emergencyStopTriggered = true),
                feedbackMessage = "EMERGENCY STOPPED / توقف طارئ فوري"
            )
        }
        speakTacticalNotification("توقف طارئ فوري!", "Emergency stop activated!")
    }

    fun loadScenario(scenario: SampleFrameProvider.Scenario) {
        _uiState.update { it.copy(activeScenario = scenario) }
        viewModelScope.launch {
            val bitmap = SampleFrameProvider.createScenarioBitmap(scenario)
            val state = visionPipeline.analyzeFrame(bitmap)
            val decision = reasoningEngine.decide(state, _uiState.value.settings)
            _uiState.update {
                it.copy(
                    perception = state,
                    decision = decision
                )
            }
        }
    }

    private suspend fun runPerceptionCycle() {
        val scenario = _uiState.value.activeScenario
        val bitmap = SampleFrameProvider.createScenarioBitmap(scenario)
        val state = visionPipeline.analyzeFrame(bitmap)
        val decision = reasoningEngine.decide(state, _uiState.value.settings)

        _uiState.update { current ->
            val event = DiagnosticEvent(
                messageEn = "Frame evaluated: ${state.healthPercentage}% HP, Threat: ${state.threatDetected.labelEn}",
                messageAr = "تحليل إطار: ${state.healthPercentage}% صحة، ${state.threatDetected.labelAr}"
            )
            val updatedEvents = (listOf(event) + current.diagnostics.lastEventLog).take(20)

            current.copy(
                perception = state,
                decision = decision,
                diagnostics = current.diagnostics.copy(
                    latencyMs = state.frameProcessingLatencyMs,
                    totalFramesAnalyzed = current.diagnostics.totalFramesAnalyzed + 1,
                    totalDecisionsExecuted = current.diagnostics.totalDecisionsExecuted + 1,
                    lastEventLog = updatedEvents
                )
            )
        }

        // Voice output if speech feedback enabled
        if (_uiState.value.settings.speechFeedbackEnabled) {
            val speechText = if (_uiState.value.settings.speechLanguageIsArabic) {
                decision.voiceSpeechAr
            } else {
                decision.voiceSpeechEn
            }
            voiceAssistant?.speak(
                text = speechText,
                isArabic = _uiState.value.settings.speechLanguageIsArabic,
                rate = _uiState.value.settings.speechSpeechRate,
                pitch = 1.0f
            )
        }
    }

    fun selectProfile(profile: TacticalProfileType) {
        _uiState.update {
            val newSettings = it.settings.copy(
                profileType = profile,
                aggression = profile.defaultAggression,
                survivalPriority = profile.defaultSurvivalPriority
            )
            val newDecision = reasoningEngine.decide(it.perception, newSettings)
            it.copy(
                settings = newSettings,
                decision = newDecision,
                feedbackMessage = "Profile: ${profile.titleEn} / ${profile.titleAr}"
            )
        }
        speakTacticalNotification("تم تغيير الاستراتيجية إلى ${profile.titleAr}", "Profile switched to ${profile.titleEn}")
    }

    fun updateAggression(aggression: Int) {
        _uiState.update {
            val newSettings = it.settings.copy(aggression = aggression)
            it.copy(
                settings = newSettings,
                decision = reasoningEngine.decide(it.perception, newSettings)
            )
        }
    }

    fun updateSurvivalPriority(survival: Int) {
        _uiState.update {
            val newSettings = it.settings.copy(survivalPriority = survival)
            it.copy(
                settings = newSettings,
                decision = reasoningEngine.decide(it.perception, newSettings)
            )
        }
    }

    fun toggleSpeechLanguage() {
        _uiState.update {
            it.copy(
                settings = it.settings.copy(
                    speechLanguageIsArabic = !it.settings.speechLanguageIsArabic
                )
            )
        }
    }

    fun toggleSpeechFeedback() {
        _uiState.update {
            it.copy(
                settings = it.settings.copy(
                    speechFeedbackEnabled = !it.settings.speechFeedbackEnabled
                )
            )
        }
    }

    fun toggleHighContrast() {
        _uiState.update {
            it.copy(
                settings = it.settings.copy(
                    highContrastMode = !it.settings.highContrastMode
                )
            )
        }
    }

    fun setSpeechRate(rate: Float) {
        _uiState.update {
            it.copy(settings = it.settings.copy(speechSpeechRate = rate))
        }
    }

    fun startVoiceListening() {
        voiceAssistant?.startListening(_uiState.value.settings.speechLanguageIsArabic)
    }

    fun stopVoiceListening() {
        voiceAssistant?.stopListening()
    }

    private fun handleVoiceCommand(command: ParsedVoiceCommand) {
        _uiState.update { it.copy(lastVoiceCommandText = command.rawText) }

        when (command) {
            is ParsedVoiceCommand.EmergencyStop -> triggerEmergencyStop()
            is ParsedVoiceCommand.StartAssistance -> startAutopilot()
            is ParsedVoiceCommand.ExplainScene -> {
                val p = _uiState.value.perception
                val speech = if (_uiState.value.settings.speechLanguageIsArabic) {
                    "صحتك ${p.healthPercentage} بالمئة. ${p.recommendedSafeActionAr}"
                } else {
                    "Health at ${p.healthPercentage} percent. ${p.recommendedSafeAction}"
                }
                voiceAssistant?.speak(speech, _uiState.value.settings.speechLanguageIsArabic)
            }
            is ParsedVoiceCommand.NavigateToZone -> {
                selectProfile(TacticalProfileType.NAVIGATION_GUIDE)
            }
            is ParsedVoiceCommand.ChangeProfile -> {
                selectProfile(command.profile)
            }
            is ParsedVoiceCommand.CheckDiagnostics -> {
                refreshDiagnostics()
                val diag = _uiState.value.diagnostics
                val speech = if (_uiState.value.settings.speechLanguageIsArabic) {
                    "حالة الاتصال: نمط التوجيه الصوتي الآمن يعمل بنجاح."
                } else {
                    "Connection status: Tactical audio guidance is active."
                }
                voiceAssistant?.speak(speech, _uiState.value.settings.speechLanguageIsArabic)
            }
            is ParsedVoiceCommand.Unknown -> {
                speakTacticalNotification("أمر غير مفهوم، كرر من فضلك", "Command not understood, please repeat")
            }
        }
    }

    fun pairWirelessAdb(portStr: String, codeStr: String) {
        val port = portStr.toIntOrNull() ?: 0
        val result = capabilityManager.pairWirelessAdb(port, codeStr)
        _uiState.update {
            it.copy(
                feedbackMessage = result.second,
                diagnostics = it.diagnostics.copy(
                    isWirelessAdbPaired = result.first,
                    wirelessAdbPort = if (result.first) port else 0
                )
            )
        }
    }

    fun requestShizukuPermission() {
        val success = capabilityManager.requestShizukuPermission(777)
        _uiState.update {
            it.copy(
                feedbackMessage = if (success) "Shizuku permission requested" else "Shizuku service not responding"
            )
        }
        refreshDiagnostics()
    }

    private fun speakTacticalNotification(arabicText: String, englishText: String) {
        val text = if (_uiState.value.settings.speechLanguageIsArabic) arabicText else englishText
        voiceAssistant?.speak(text, _uiState.value.settings.speechLanguageIsArabic)
    }

    private fun vibrateEmergency() {
        try {
            val vibrator = getApplication<Application>().getSystemService(Vibrator::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(400)
            }
        } catch (_: Exception) {}
    }

    override fun onCleared() {
        super.onCleared()
        loopJob?.cancel()
        voiceAssistant?.shutdown()
    }
}
