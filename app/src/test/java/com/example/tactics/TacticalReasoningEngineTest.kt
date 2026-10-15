package com.example.tactics

import com.example.model.DecisionUrgency
import com.example.model.PerceptionState
import com.example.model.TacticalActionType
import com.example.model.TacticalProfileType
import com.example.model.TacticalSettings
import com.example.model.ThreatSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TacticalReasoningEngineTest {

    private val engine = TacticalReasoningEngine()

    @Test
    fun testCriticalHealthTriggersHealDecision() {
        val lowHealthPerception = PerceptionState(
            healthPercentage = 20,
            threatDetected = ThreatSeverity.NONE
        )
        val settings = TacticalSettings(healthWarningThreshold = 35)

        val decision = engine.decide(lowHealthPerception, settings)

        assertEquals(TacticalActionType.HEAL, decision.actionType)
        assertEquals(DecisionUrgency.CRITICAL, decision.urgency)
        assertTrue(decision.voiceSpeechAr.contains("تحذير"))
    }

    @Test
    fun testZoneShrinkingTriggersNavigation() {
        val zonePerception = PerceptionState(
            healthPercentage = 100,
            zoneTimerSeconds = 10,
            isZoneShrinking = true,
            recommendedHeadingDegrees = 45f
        )
        val settings = TacticalSettings()

        val decision = engine.decide(zonePerception, settings)

        assertEquals(TacticalActionType.MOVE_TO_ZONE, decision.actionType)
        assertEquals(DecisionUrgency.WARNING, decision.urgency)
        assertTrue(decision.voiceSpeechAr.contains("الزون"))
    }

    @Test
    fun testAggressiveProfileEngagesEnemy() {
        val enemyPerception = PerceptionState(
            healthPercentage = 90,
            threatDetected = ThreatSeverity.CRITICAL,
            threatClockDirection = 2,
            threatDistanceMeters = 30
        )
        val aggressiveSettings = TacticalSettings(
            profileType = TacticalProfileType.AGGRESSIVE_RUSHER,
            aggression = 85
        )

        val decision = engine.decide(enemyPerception, aggressiveSettings)

        assertEquals(TacticalActionType.ENGAGE_TARGET, decision.actionType)
        assertEquals(DecisionUrgency.CRITICAL, decision.urgency)
        assertTrue(decision.voiceSpeechAr.contains("اشتباك"))
    }

    @Test
    fun testDefensiveProfileTakesCoverFromEnemy() {
        val enemyPerception = PerceptionState(
            healthPercentage = 90,
            threatDetected = ThreatSeverity.CRITICAL,
            threatClockDirection = 1,
            threatDistanceMeters = 35
        )
        val defensiveSettings = TacticalSettings(
            profileType = TacticalProfileType.DEFENSIVE_SURVIVOR,
            aggression = 20
        )

        val decision = engine.decide(enemyPerception, defensiveSettings)

        assertEquals(TacticalActionType.TAKE_COVER, decision.actionType)
        assertEquals(DecisionUrgency.WARNING, decision.urgency)
        assertTrue(decision.voiceSpeechAr.contains("انتبه"))
    }
}
