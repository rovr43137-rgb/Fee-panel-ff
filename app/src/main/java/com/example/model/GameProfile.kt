package com.example.model

/**
 * Definition of target game characteristics and HUD regions.
 */
data class GameProfile(
    val id: String,
    val displayName: String,
    val packageName: String,
    val isAntiCheatStrict: Boolean,
    val supportedModeDescriptionEn: String,
    val supportedModeDescriptionAr: String,
    val healthBarNormalizedRegion: NormalizedRect,
    val minimapNormalizedRegion: NormalizedRect,
    val weaponSlotNormalizedRegion: NormalizedRect
) {
    companion object {
        val FREE_FIRE = GameProfile(
            id = "free_fire",
            displayName = "Garena Free Fire",
            packageName = "com.dts.freefireth",
            isAntiCheatStrict = true,
            supportedModeDescriptionEn = "HUD Audio Narration, Voice Navigation & Tactical Guidance (Fair-play compliant)",
            supportedModeDescriptionAr = "توجيه صوتي للواجهة، ملاحة بالأوامر الصوتية وإرشاد تكتيكي (متوافق مع سياسات اللعب النظيف)",
            healthBarNormalizedRegion = NormalizedRect(left = 0.35f, top = 0.88f, right = 0.65f, bottom = 0.95f),
            minimapNormalizedRegion = NormalizedRect(left = 0.02f, top = 0.02f, right = 0.22f, bottom = 0.25f),
            weaponSlotNormalizedRegion = NormalizedRect(left = 0.70f, top = 0.02f, right = 0.98f, bottom = 0.15f)
        )

        val FREE_FIRE_MAX = GameProfile(
            id = "free_fire_max",
            displayName = "Garena Free Fire MAX",
            packageName = "com.dts.freefiremax",
            isAntiCheatStrict = true,
            supportedModeDescriptionEn = "High-definition HUD Narration & Voice Tactical Coach",
            supportedModeDescriptionAr = "توجيه صوتي عالي الدقة لشاشات MAX ومساعد تكتيكي صوتي",
            healthBarNormalizedRegion = NormalizedRect(left = 0.35f, top = 0.88f, right = 0.65f, bottom = 0.95f),
            minimapNormalizedRegion = NormalizedRect(left = 0.02f, top = 0.02f, right = 0.22f, bottom = 0.25f),
            weaponSlotNormalizedRegion = NormalizedRect(left = 0.70f, top = 0.02f, right = 0.98f, bottom = 0.15f)
        )

        val CUSTOM_BR = GameProfile(
            id = "custom_br",
            displayName = "Generic Battle Royale",
            packageName = "custom",
            isAntiCheatStrict = false,
            supportedModeDescriptionEn = "Adaptive HUD visual detector for generic survival games",
            supportedModeDescriptionAr = "مكتشف بصري متكيف لألعاب البقاء العامة",
            healthBarNormalizedRegion = NormalizedRect(left = 0.30f, top = 0.85f, right = 0.70f, bottom = 0.95f),
            minimapNormalizedRegion = NormalizedRect(left = 0.02f, top = 0.02f, right = 0.25f, bottom = 0.25f),
            weaponSlotNormalizedRegion = NormalizedRect(left = 0.65f, top = 0.02f, right = 0.98f, bottom = 0.18f)
        )
    }
}

data class NormalizedRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)
