package com.example.model

/**
 * Diagnostics tracking system permissions, Shizuku binder, Wireless ADB,
 * and game policy compliance state.
 */
data class SystemDiagnostics(
    val hasOverlayPermission: Boolean = false,
    val hasAudioRecordPermission: Boolean = false,
    val isMediaProjectionActive: Boolean = false,
    val isShizukuInstalled: Boolean = false,
    val isShizukuRunning: Boolean = false,
    val isShizukuAuthorized: Boolean = false,
    val isWirelessAdbPaired: Boolean = false,
    val wirelessAdbPort: Int = 0,
    val activeInputMode: ActiveInputMode = ActiveInputMode.TACTICAL_AUDIO_ASSIST,
    val latencyMs: Long = 18L,
    val totalFramesAnalyzed: Long = 0L,
    val totalDecisionsExecuted: Long = 0L,
    val emergencyStopTriggered: Boolean = false,
    val lastEventLog: List<DiagnosticEvent> = emptyList()
)

enum class ActiveInputMode(val titleEn: String, val titleAr: String, val isCompliant: Boolean) {
    TACTICAL_AUDIO_ASSIST(
        titleEn = "Tactical Audio & Voice Guidance",
        titleAr = "التوجيه التكتيكي الصوتي (معتمد وآمن)",
        isCompliant = true
    ),
    SHIZUKU_ASSISTED_INPUT(
        titleEn = "Shizuku Input Adapter (Sandbox Testing)",
        titleAr = "محول إدخال شيزوكو (بيئة الاختبار)",
        isCompliant = false
    ),
    WIRELESS_ADB_ADAPTER(
        titleEn = "Wireless ADB Adapter (Development Testing)",
        titleAr = "محول تصحيح ADB اللاسلكي",
        isCompliant = false
    )
}

data class DiagnosticEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val messageEn: String,
    val messageAr: String,
    val isError: Boolean = false
)
