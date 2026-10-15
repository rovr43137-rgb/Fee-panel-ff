package com.example.model

/**
 * Tactical playstyle profiles determining how the AI reasons about game state,
 * prioritizing accessibility, safety, and combat positioning.
 */
enum class TacticalProfileType(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val descriptionEn: String,
    val descriptionAr: String,
    val defaultAggression: Int,
    val defaultSurvivalPriority: Int
) {
    ACCESSIBILITY_ASSIST(
        id = "accessibility_assist",
        titleEn = "Accessibility Assist",
        titleAr = "مساعد إمكانية الوصول",
        descriptionEn = "Continuous audio narration of health, zone, and threats with maximal safety guidance.",
        descriptionAr = "توجيه صوتي مستمر لحالة الصحة والمنطقة الآمنة والتهديدات بأقصى درجات الأمان.",
        defaultAggression = 15,
        defaultSurvivalPriority = 95
    ),
    DEFENSIVE_SURVIVOR(
        id = "defensive_survivor",
        titleEn = "Defensive Survivor",
        titleAr = "الناجي الدفاعي",
        descriptionEn = "Focuses on staying behind cover, anticipating zone shrinks, and avoiding unnecessary conflicts.",
        descriptionAr = "التركيز على البقاء خلف السواتر، وتوقع تقلص الزون، وتجنب الاشتباكات غير الضرورية.",
        defaultAggression = 25,
        defaultSurvivalPriority = 90
    ),
    BALANCED_TACTICAL(
        id = "balanced_tactical",
        titleEn = "Balanced Tactical",
        titleAr = "تكتيكي متوازن",
        descriptionEn = "Balanced engagement strategy balancing loot gathering, tactical positioning, and calculated defense.",
        descriptionAr = "استراتيجية متوازنة بين جمع العتاد، التمركز التكتيكي، والدفاع المدروس.",
        defaultAggression = 50,
        defaultSurvivalPriority = 70
    ),
    AGGRESSIVE_RUSHER(
        id = "aggressive_rusher",
        titleEn = "Aggressive Rusher",
        titleAr = "اقتحام هجومي",
        descriptionEn = "Prioritizes immediate threat neutralization, high-tempo mobility, and forward pressure.",
        descriptionAr = "أولوية لتحييد الأهداف بسرعة، حركة ديناميكية مستمرة، وضغط هجومي مباشر.",
        defaultAggression = 85,
        defaultSurvivalPriority = 45
    ),
    NAVIGATION_GUIDE(
        id = "navigation_guide",
        titleEn = "Navigation & Zone Guide",
        titleAr = "مرشد الملاحة والزون",
        descriptionEn = "Dedicated waypoint guidance, safe terrain pathfinding, and blue zone countdown alerts.",
        descriptionAr = "توجيه ملاحي مخصص للطرق الآمنة، وتنبيهات مستمرة لعد تنازلي لمنطقة الزون.",
        defaultAggression = 20,
        defaultSurvivalPriority = 85
    )
}

/**
 * User-configurable parameters for the Tactical Decision Engine.
 */
data class TacticalSettings(
    val profileType: TacticalProfileType = TacticalProfileType.ACCESSIBILITY_ASSIST,
    val aggression: Int = TacticalProfileType.ACCESSIBILITY_ASSIST.defaultAggression,
    val survivalPriority: Int = TacticalProfileType.ACCESSIBILITY_ASSIST.defaultSurvivalPriority,
    val healthWarningThreshold: Int = 35,
    val speechFeedbackEnabled: Boolean = true,
    val speechLanguageIsArabic: Boolean = true,
    val speechSpeechRate: Float = 1.0f,
    val speechVolume: Float = 1.0f,
    val hapticFeedbackEnabled: Boolean = true,
    val highContrastMode: Boolean = true,
    val frameAnalysisIntervalMs: Long = 1000L
)
