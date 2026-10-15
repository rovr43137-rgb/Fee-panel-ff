package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.model.TacticalProfileType
import java.util.Locale

/**
 * Handles voice-driven accessibility: Arabic & English speech recognition
 * and spoken tactical audio narration.
 */
class VoiceAssistantManager(
    private val context: Context,
    private val onCommandReceived: (ParsedVoiceCommand) -> Unit,
    private val onListeningStateChanged: (Boolean) -> Unit
) : RecognitionListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false

    init {
        initTts()
    }

    private fun initTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                // Default to Arabic locale
                val arabic = Locale("ar")
                if (textToSpeech?.isLanguageAvailable(arabic) == TextToSpeech.LANG_AVAILABLE) {
                    textToSpeech?.language = arabic
                } else {
                    textToSpeech?.language = Locale.US
                }
            }
        }
    }

    fun speak(text: String, isArabic: Boolean, rate: Float = 1.0f, pitch: Float = 1.0f) {
        if (!isTtsReady) return
        textToSpeech?.apply {
            language = if (isArabic) Locale("ar") else Locale.US
            setSpeechRate(rate)
            setPitch(pitch)
            speak(text, TextToSpeech.QUEUE_FLUSH, null, "tactical_utterance_${System.currentTimeMillis()}")
        }
    }

    fun startListening(useArabic: Boolean) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            if (speechRecognizer == null) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(this@VoiceAssistantManager)
                }
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, if (useArabic) "ar-SA" else "en-US")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            }
            try {
                speechRecognizer?.startListening(intent)
                onListeningStateChanged(true)
            } catch (e: Exception) {
                onListeningStateChanged(false)
            }
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        onListeningStateChanged(false)
    }

    fun shutdown() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            textToSpeech = null
        } catch (_: Exception) {}
    }

    // RecognitionListener overrides
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {
        onListeningStateChanged(false)
    }
    override fun onError(error: Int) {
        onListeningStateChanged(false)
    }

    override fun onResults(results: Bundle?) {
        onListeningStateChanged(false)
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) ?: return
        if (matches.isNotEmpty()) {
            val spokenText = matches[0]
            val command = parseCommand(spokenText)
            onCommandReceived(command)
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}

    companion object {
        fun parseCommand(text: String): ParsedVoiceCommand {
            val lower = text.lowercase().trim()
            return when {
                lower.contains("توقف") || lower.contains("قف") || lower.contains("stop") || lower.contains("halt") ->
                    ParsedVoiceCommand.EmergencyStop(raw = text)

                lower.contains("ابدأ") || lower.contains("شغل") || lower.contains("start") || lower.contains("begin") ->
                    ParsedVoiceCommand.StartAssistance(raw = text)

                lower.contains("اشرح") || lower.contains("ما يحدث") || lower.contains("explain") || lower.contains("status") ->
                    ParsedVoiceCommand.ExplainScene(raw = text)

                lower.contains("طريق") || lower.contains("الزون") || lower.contains("ملاحة") || lower.contains("nav") || lower.contains("zone") ->
                    ParsedVoiceCommand.NavigateToZone(raw = text)

                lower.contains("دفاع") || lower.contains("دفاعية") || lower.contains("defensive") ->
                    ParsedVoiceCommand.ChangeProfile(profile = TacticalProfileType.DEFENSIVE_SURVIVOR, raw = text)

                lower.contains("هجوم") || lower.contains("اقتحام") || lower.contains("aggressive") || lower.contains("rush") ->
                    ParsedVoiceCommand.ChangeProfile(profile = TacticalProfileType.AGGRESSIVE_RUSHER, raw = text)

                lower.contains("اتصال") || lower.contains("شيزوكو") || lower.contains("connection") || lower.contains("diagnostics") ->
                    ParsedVoiceCommand.CheckDiagnostics(raw = text)

                else -> ParsedVoiceCommand.Unknown(raw = text)
            }
        }
    }
}

sealed class ParsedVoiceCommand(val rawText: String) {
    class StartAssistance(raw: String) : ParsedVoiceCommand(raw)
    class EmergencyStop(raw: String) : ParsedVoiceCommand(raw)
    class ExplainScene(raw: String) : ParsedVoiceCommand(raw)
    class NavigateToZone(raw: String) : ParsedVoiceCommand(raw)
    class ChangeProfile(val profile: TacticalProfileType, raw: String) : ParsedVoiceCommand(raw)
    class CheckDiagnostics(raw: String) : ParsedVoiceCommand(raw)
    class Unknown(raw: String) : ParsedVoiceCommand(raw)
}
