package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightSlate800 = Color(0xFF1E293B)
val LightSlate500 = Color(0xFF64748B)

private val LightCleanColorScheme = lightColorScheme(
    primary = DeepBlue,
    onPrimary = PureWhite,
    primaryContainer = SoftLightBlue,
    onPrimaryContainer = DeepBlue,
    secondary = GradientAccentBlue,
    onSecondary = PureWhite,
    secondaryContainer = SoftLightBlue,
    onSecondaryContainer = DeepBlue,
    background = Color(0xFFF7F9FC), // Clean Light Background
    onBackground = LightSlate800,   // Slate 800 Primary Text
    surface = PureWhite,           // Clean White Surface
    onSurface = LightSlate800,      // Slate 800 Surface Text
    surfaceVariant = SoftLightBlue,
    onSurfaceVariant = LightSlate500 // Slate 500 Muted Text
)

@Composable
fun SnowWhiteTheme(
    darkTheme: Boolean = false, // Force Light Theme across the entire app
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightCleanColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias if needed
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SnowWhiteTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
