package com.example.tactics

import com.example.model.DecisionUrgency
import com.example.model.PerceptionState
import com.example.model.TacticalActionType
import com.example.model.TacticalDecision
import com.example.model.TacticalProfileType
import com.example.model.TacticalSettings
import com.example.model.ThreatSeverity

/**
 * Tactical decision-making engine combining perception, profile constraints,
 * and safety priority rules into actionable advice and speech.
 */
class TacticalReasoningEngine {

    fun decide(
        perception: PerceptionState,
        settings: TacticalSettings
    ): TacticalDecision {
        // 1. Critical Health Alert
        if (perception.healthPercentage <= settings.healthWarningThreshold) {
            return TacticalDecision(
                titleEn = "Critical Health Warning (${perception.healthPercentage}%)",
                titleAr = "تحذير صحة حرجة (${perception.healthPercentage}%)",
                rationaleEn = "Health is below safety threshold. Seek solid cover and apply Medkit immediately.",
                rationaleAr = "مستوى الصحة منخفض جداً. ادخل خلف أقرب ساتر واستخدم حقيبة الإسعاف فوراً.",
                voiceSpeechAr = "تحذير: صحتك ${perception.healthPercentage} بالمئة! احتمِ فوراً واستخدم الإسعاف.",
                voiceSpeechEn = "Warning! Health at ${perception.healthPercentage} percent. Take cover and heal now.",
                urgency = DecisionUrgency.CRITICAL,
                actionType = TacticalActionType.HEAL
            )
        }

        // 2. Zone Imminent Warning
        if (perception.isZoneShrinking || perception.zoneTimerSeconds <= 15) {
            val heading = perception.recommendedHeadingDegrees.toInt()
            return TacticalDecision(
                titleEn = "Zone Shrinking in ${perception.zoneTimerSeconds}s",
                titleAr = "الزون يتقلص خلال ${perception.zoneTimerSeconds} ثانية",
                rationaleEn = "The blue danger zone is closing. Move immediately toward safe zone heading $heading°.",
                rationaleAr = "منطقة الزون الخطرة تقترب. تحرك فوراً باتجاه زاوية $heading درجة نحو المنطقة البيضاء.",
                voiceSpeechAr = "تنبيه الزون! بقي ${perception.zoneTimerSeconds} ثانية. تحرك نحو المنطقة الآمنة.",
                voiceSpeechEn = "Zone alert! ${perception.zoneTimerSeconds} seconds left. Move to safe zone now.",
                urgency = DecisionUrgency.WARNING,
                actionType = TacticalActionType.MOVE_TO_ZONE
            )
        }

        // 3. Threat Handling by Profile
        when (perception.threatDetected) {
            ThreatSeverity.CRITICAL -> {
                val clock = perception.threatClockDirection
                return if (settings.profileType == TacticalProfileType.AGGRESSIVE_RUSHER && settings.aggression >= 70) {
                    TacticalDecision(
                        titleEn = "Target Engaged ($clock o'clock)",
                        titleAr = "اشتباك مع هدف (الساعة $clock)",
                        rationaleEn = "Enemy detected at $clock o'clock at ${perception.threatDistanceMeters}m. Aim and fire short bursts.",
                        rationaleAr = "عدو مباشر عند اتجاه الساعة $clock على بعد ${perception.threatDistanceMeters} متراً. صوّب وأطلق دفعات قصيرة.",
                        voiceSpeechAr = "عدو مباشر عند الساعة $clock! استعد للاشتباك واطلق النار.",
                        voiceSpeechEn = "Target at $clock o'clock! Prepare to engage.",
                        urgency = DecisionUrgency.CRITICAL,
                        actionType = TacticalActionType.ENGAGE_TARGET
                    )
                } else {
                    TacticalDecision(
                        titleEn = "Enemy Threat ($clock o'clock)",
                        titleAr = "تهديد مباشر (الساعة $clock)",
                        rationaleEn = "Enemy presence at $clock o'clock. Break line of sight and hold defensive position.",
                        rationaleAr = "رصد عدو عند الساعة $clock. اقطع خط الرؤية وتمركز خلف ساتر دفاعي.",
                        voiceSpeechAr = "انتبه! عدو عند الساعة $clock على بعد ${perception.threatDistanceMeters} متراً. خذ ساتراً.",
                        voiceSpeechEn = "Danger! Enemy at $clock o'clock, ${perception.threatDistanceMeters} meters away. Take cover.",
                        urgency = DecisionUrgency.WARNING,
                        actionType = TacticalActionType.TAKE_COVER
                    )
                }
            }
            ThreatSeverity.MEDIUM -> {
                val clock = perception.threatClockDirection
                return TacticalDecision(
                    titleEn = "Gunfire Detected ($clock o'clock)",
                    titleAr = "أصوات اشتباك (الساعة $clock)",
                    rationaleEn = "Combat sounds reported nearby. Maintain awareness and crouch behind cover.",
                    rationaleAr = "أصوات رصاص قريبة عند اتجاه الساعة $clock. انحنِ خلف ساتر وراقب المحيط.",
                    voiceSpeechAr = "اشتباك قريب عند الساعة $clock. حافظ على التخفي وراقب الحركة.",
                    voiceSpeechEn = "Gunfire detected at $clock o'clock. Stay low.",
                    urgency = DecisionUrgency.WARNING,
                    actionType = TacticalActionType.HOLD_POSITION
                )
            }
            ThreatSeverity.LOW -> {
                return TacticalDecision(
                    titleEn = "Distant Movement",
                    titleAr = "حركة على مسافة بعيدة",
                    rationaleEn = "Movement identified at distance. Observe without exposing position.",
                    rationaleAr = "تمت ملاحظة حركة بعيدة في الأفق. راقب الموقع دون كشف تمركزك.",
                    voiceSpeechAr = "حركة بعيدة في الأفق. راقب بحذر.",
                    voiceSpeechEn = "Distant movement noticed. Observe carefully.",
                    urgency = DecisionUrgency.INFO,
                    actionType = TacticalActionType.HOLD_POSITION
                )
            }
            ThreatSeverity.NONE -> {
                return TacticalDecision(
                    titleEn = "Sector Secure & Advancing",
                    titleAr = "القطاع آمن والتقدم مستمر",
                    rationaleEn = "No active hostiles in minimap sensor radius. Advance safely and gather equipment.",
                    rationaleAr = "لا توجد أهداف معادية في نطاق الخريطة. تقدم بأمان واجمع العتاد والدروع.",
                    voiceSpeechAr = "المنطقة آمنة تماماً. واصل التقدم وتجميع العتاد.",
                    voiceSpeechEn = "Sector is clear. Proceed with looting and advancing.",
                    urgency = DecisionUrgency.INFO,
                    actionType = TacticalActionType.HOLD_POSITION
                )
            }
        }
    }
}
