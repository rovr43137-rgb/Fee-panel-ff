package com.example.perception

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.model.PerceptionState
import com.example.model.ThreatSeverity

/**
 * Provides representative gameplay scenario frames for testbench verification,
 * benchmarking, and offline vision pipeline calibration.
 */
object SampleFrameProvider {

    enum class Scenario(
        val titleEn: String,
        val titleAr: String,
        val expectedState: PerceptionState
    ) {
        COMBAT_CRITICAL(
            titleEn = "Direct Combat (Low Health)",
            titleAr = "اشتباك مباشر (صحة حرجة)",
            expectedState = PerceptionState(
                healthPercentage = 22,
                armorLevel = 1,
                primaryWeapon = "AK47",
                ammoCount = 14,
                zoneTimerSeconds = 65,
                isZoneShrinking = false,
                threatDetected = ThreatSeverity.CRITICAL,
                threatClockDirection = 1,
                threatDistanceMeters = 35,
                recommendedHeadingDegrees = 210f,
                recommendedSafeAction = "Retreat behind left concrete wall, equip Medkit",
                recommendedSafeActionAr = "تراجع فوراً خلف الساتر الخرساني على اليسار واستخدم حقيبة الإسعاف",
                confidence = 0.96f
            )
        ),
        ZONE_SHRINKING(
            titleEn = "Safe Zone Shrinking",
            titleAr = "تقلص المنطقة الآمنة (الزون)",
            expectedState = PerceptionState(
                healthPercentage = 85,
                armorLevel = 3,
                primaryWeapon = "SCAR-L",
                ammoCount = 30,
                zoneTimerSeconds = 12,
                isZoneShrinking = true,
                threatDetected = ThreatSeverity.LOW,
                threatClockDirection = 6,
                threatDistanceMeters = 120,
                recommendedHeadingDegrees = 45f,
                recommendedSafeAction = "Sprint northeast towards the safe white circle",
                recommendedSafeActionAr = "اركض باتجاه الشمال الشرقي نحو الدائرة البيضاء الآمنة",
                confidence = 0.93f
            )
        ),
        SNIPING_POSITION(
            titleEn = "Elevated Tactical Overwatch",
            titleAr = "موقع مراقبة مرتفع (قنص)",
            expectedState = PerceptionState(
                healthPercentage = 100,
                armorLevel = 3,
                primaryWeapon = "AWM Sniper",
                ammoCount = 5,
                zoneTimerSeconds = 140,
                isZoneShrinking = false,
                threatDetected = ThreatSeverity.MEDIUM,
                threatClockDirection = 11,
                threatDistanceMeters = 180,
                recommendedHeadingDegrees = 330f,
                recommendedSafeAction = "Maintain high ground cover, scan 11 o'clock ridge",
                recommendedSafeActionAr = "ابقَ في الساتر المرتفع، وراقب التل عند اتجاه الساعة 11",
                confidence = 0.95f
            )
        ),
        EXPLORATION_CLEAR(
            titleEn = "Looting & Clear Surroundings",
            titleAr = "جمع العتاد ومحيط آمن",
            expectedState = PerceptionState(
                healthPercentage = 100,
                armorLevel = 2,
                primaryWeapon = "MP40",
                ammoCount = 32,
                zoneTimerSeconds = 190,
                isZoneShrinking = false,
                threatDetected = ThreatSeverity.NONE,
                threatClockDirection = 12,
                threatDistanceMeters = 0,
                recommendedHeadingDegrees = 0f,
                recommendedSafeAction = "Area clear. Collect armor upgrades and ammo",
                recommendedSafeActionAr = "المنطقة آمنة تماماً. اجمع ترقيات الدرع والذخيرة",
                confidence = 0.98f
            )
        )
    }

    /**
     * Generates a synthetic 720x1280 gameplay HUD frame simulating the scenario.
     */
    fun createScenarioBitmap(scenario: Scenario): Bitmap {
        val width = 720
        val height = 1280
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw game terrain background (dark stylized terrain)
        val bgPaint = Paint().apply {
            color = Color.rgb(20, 32, 26)
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Draw Minimap (top left)
        val mapPaint = Paint().apply {
            color = Color.rgb(30, 45, 60)
            style = Paint.Style.FILL
        }
        val mapStroke = Paint().apply {
            color = Color.CYAN
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        val mapRadius = 90f
        val mapCenterX = 110f
        val mapCenterY = 110f
        canvas.drawCircle(mapCenterX, mapCenterY, mapRadius, mapPaint)
        canvas.drawCircle(mapCenterX, mapCenterY, mapRadius, mapStroke)

        // Draw threat indicator on minimap if present
        if (scenario.expectedState.threatDetected != ThreatSeverity.NONE) {
            val threatPaint = Paint().apply {
                color = Color.RED
                style = Paint.Style.FILL
            }
            // Draw red dot at clock position
            val angleRad = Math.toRadians((scenario.expectedState.threatClockDirection * 30 - 90).toDouble())
            val dotX = (mapCenterX + (mapRadius * 0.7f * Math.cos(angleRad))).toFloat()
            val dotY = (mapCenterY + (mapRadius * 0.7f * Math.sin(angleRad))).toFloat()
            canvas.drawCircle(dotX, dotY, 14f, threatPaint)
        }

        // Draw Health Bar (bottom center)
        val barBg = Paint().apply { color = Color.DKGRAY }
        val barFill = Paint().apply {
            color = if (scenario.expectedState.healthPercentage < 30) Color.RED else Color.GREEN
        }
        val left = width * 0.35f
        val top = height * 0.88f
        val right = width * 0.65f
        val bottom = height * 0.92f

        canvas.drawRoundRect(RectF(left, top, right, bottom), 8f, 8f, barBg)
        val filledRight = left + (right - left) * (scenario.expectedState.healthPercentage / 100f)
        canvas.drawRoundRect(RectF(left, top, filledRight, bottom), 8f, 8f, barFill)

        return bitmap
    }
}
