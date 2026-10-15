package com.example.model

/**
 * Real-time perceived state extracted from game screen frames.
 */
data class PerceptionState(
    val healthPercentage: Int = 100,
    val armorLevel: Int = 2,
    val primaryWeapon: String = "M4A1",
    val ammoCount: Int = 30,
    val zoneTimerSeconds: Int = 85,
    val isZoneShrinking: Boolean = false,
    val threatDetected: ThreatSeverity = ThreatSeverity.NONE,
    val threatClockDirection: Int = 12, // 1..12 clock face
    val threatDistanceMeters: Int = 0,
    val recommendedHeadingDegrees: Float = 0f,
    val recommendedSafeAction: String = "Advance to safe zone cover",
    val recommendedSafeActionAr: String = "تقدم نحو الساتر في المنطقة الآمنة",
    val confidence: Float = 0.94f,
    val frameProcessingLatencyMs: Long = 18L,
    val timestampMs: Long = System.currentTimeMillis()
)

enum class ThreatSeverity(val labelEn: String, val labelAr: String) {
    NONE("Clear / آمن", "لا توجد تهديدات"),
    LOW("Distant Movement / حركة بعيدة", "حركة على مسافة بعيدة"),
    MEDIUM("Gunfire Nearby / إطلاق نار قريب", "أصوات اشتباك في المحيط"),
    CRITICAL("Direct Enemy Engaged / اشتباك مباشر", "عدو في المدى المباشر")
}

/**
 * Tactical decision produced by the Reasoning Engine.
 */
data class TacticalDecision(
    val titleEn: String,
    val titleAr: String,
    val rationaleEn: String,
    val rationaleAr: String,
    val voiceSpeechAr: String,
    val voiceSpeechEn: String,
    val urgency: DecisionUrgency,
    val actionType: TacticalActionType,
    val timestamp: Long = System.currentTimeMillis()
)

enum class DecisionUrgency {
    INFO,
    WARNING,
    CRITICAL
}

enum class TacticalActionType {
    HEAL,
    TAKE_COVER,
    MOVE_TO_ZONE,
    ENGAGE_TARGET,
    RELOAD,
    HOLD_POSITION
}
