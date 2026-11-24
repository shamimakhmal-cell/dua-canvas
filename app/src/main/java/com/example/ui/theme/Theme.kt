package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MutedIslamicGold,
    onPrimary = DarkCharcoal,
    primaryContainer = DarkForestGreen,
    onPrimaryContainer = WarmIvory,
    secondary = MutedIslamicGold,
    onSecondary = DarkCharcoal,
    background = DarkBackground,
    surface = DarkCardSurface,
    onBackground = DarkTextIvory,
    onSurface = DarkTextIvory,
    surfaceVariant = DarkForestGreen,
    onSurfaceVariant = DarkTextMuted
)

private val LightColorScheme = lightColorScheme(
    primary = DeepEmeraldGreen,
    onPrimary = WarmIvory,
    primaryContainer = WarmIvory,
    onPrimaryContainer = DeepEmeraldGreen,
    secondary = MutedIslamicGold,
    onSecondary = DarkCharcoal,
    background = WarmIvory,
    surface = Color.White,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal,
    surfaceVariant = Color(0xFFEFECE1),
    onSurfaceVariant = Color(0xFF4A5568)
)

@Composable
fun DuaCanvasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
