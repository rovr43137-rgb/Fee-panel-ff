package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NexusDarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = SpaceBlack,
    primaryContainer = SurfaceElevated,
    onPrimaryContainer = CyberCyan,
    secondary = HyperViolet,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceDark,
    onSecondaryContainer = TextPrimary,
    tertiary = NeonGreen,
    onTertiary = SpaceBlack,
    background = SpaceBlack,
    onBackground = TextPrimary,
    surface = DeepVoid,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    error = AlertRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Nexus utilizes the futuristic high-contrast space theme for consistent accessibility
    MaterialTheme(
        colorScheme = NexusDarkColorScheme,
        typography = Typography,
        content = content
    )
}
