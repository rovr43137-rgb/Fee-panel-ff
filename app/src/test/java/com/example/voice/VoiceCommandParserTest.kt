package com.example.voice

import com.example.model.TacticalProfileType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoiceCommandParserTest {

    @Test
    fun testParseEmergencyStopArabic() {
        val cmd = VoiceAssistantManager.parseCommand("توقف فوراً")
        assertTrue(cmd is ParsedVoiceCommand.EmergencyStop)
    }

    @Test
    fun testParseStartAssistanceArabic() {
        val cmd = VoiceAssistantManager.parseCommand("ابدأ المساعدة الآن")
        assertTrue(cmd is ParsedVoiceCommand.StartAssistance)
    }

    @Test
    fun testParseExplainSceneArabic() {
        val cmd = VoiceAssistantManager.parseCommand("اشرح لي ما يحدث")
        assertTrue(cmd is ParsedVoiceCommand.ExplainScene)
    }

    @Test
    fun testParseNavigationArabic() {
        val cmd = VoiceAssistantManager.parseCommand("ساعدني في معرفة طريق الزون")
        assertTrue(cmd is ParsedVoiceCommand.NavigateToZone)
    }

    @Test
    fun testParseChangeProfileDefensive() {
        val cmd = VoiceAssistantManager.parseCommand("اجعل الاستراتيجية دفاعية")
        assertTrue(cmd is ParsedVoiceCommand.ChangeProfile)
        assertEquals(TacticalProfileType.DEFENSIVE_SURVIVOR, (cmd as ParsedVoiceCommand.ChangeProfile).profile)
    }

    @Test
    fun testParseChangeProfileAggressive() {
        val cmd = VoiceAssistantManager.parseCommand("نريد اقتحام وهجوم")
        assertTrue(cmd is ParsedVoiceCommand.ChangeProfile)
        assertEquals(TacticalProfileType.AGGRESSIVE_RUSHER, (cmd as ParsedVoiceCommand.ChangeProfile).profile)
    }

    @Test
    fun testParseEnglishCommands() {
        assertTrue(VoiceAssistantManager.parseCommand("emergency stop now") is ParsedVoiceCommand.EmergencyStop)
        assertTrue(VoiceAssistantManager.parseCommand("start assist") is ParsedVoiceCommand.StartAssistance)
    }
}
