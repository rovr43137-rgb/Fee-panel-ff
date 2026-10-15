package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PerceptionState
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.WarningGold

@Composable
fun TacticalHudGauges(
    perception: PerceptionState,
    modifier: Modifier = Modifier
) {
    val healthFraction by animateFloatAsState(
        targetValue = (perception.healthPercentage / 100f).coerceIn(0f, 1f),
        label = "HealthAnim"
    )

    val healthColor by animateColorAsState(
        targetValue = when {
            perception.healthPercentage <= 30 -> AlertRed
            perception.healthPercentage <= 60 -> WarningGold
            else -> NeonGreen
        },
        label = "HealthColorAnim"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("tactical_hud_gauges")
    ) {
        // Health Bar Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Health Indicator",
                    tint = healthColor,
                    modifier = Modifier.padding(end = 6.dp)
                )
                Text(
                    text = "HEALTH / الصحة",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${perception.healthPercentage}%",
                style = MaterialTheme.typography.titleMedium,
                color = healthColor,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Health Progress Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(SurfaceElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(healthFraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(7.dp))
                    .background(healthColor)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metric Badges (Armor, Weapon, Ammo, Zone Countdown)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Armor
            HudStatBadge(
                label = "ARMOR",
                labelAr = "الدرع",
                value = "Lvl ${perception.armorLevel}",
                icon = Icons.Default.Security,
                color = CyberCyan
            )

            // Weapon & Ammo
            HudStatBadge(
                label = "AMMO",
                labelAr = "الذخيرة",
                value = "${perception.ammoCount} (${perception.primaryWeapon})",
                icon = null,
                color = Color.White
            )

            // Zone Timer
            HudStatBadge(
                label = "SAFE ZONE",
                labelAr = "الزون",
                value = "${perception.zoneTimerSeconds}s",
                icon = Icons.Default.Timer,
                color = if (perception.zoneTimerSeconds < 20) AlertRed else WarningGold
            )
        }
    }
}

@Composable
private fun HudStatBadge(
    label: String,
    labelAr: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    color: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceElevated)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.height(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = "$label • $labelAr",
                fontSize = 9.sp,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
