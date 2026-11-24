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
    primary = GoldPrimary,
    onPrimary = Color.Black,
    primaryContainer = GoldDark,
    secondary = IslamicGreenLight,
    onSecondary = Color.White,
    background = Color(0xFF111816),
    surface = Color(0xFF1A2421),
    onBackground = Color(0xFFFDFBF7),
    onSurface = Color(0xFFFDFBF7)
)

private val LightColorScheme = lightColorScheme(
    primary = IslamicGreen,
    onPrimary = Color.White,
    primaryContainer = GoldLight,
    secondary = GoldPrimary,
    onSecondary = Color.Black,
    background = IvoryBackground,
    surface = SurfaceCard,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun DuaCanvasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Default to Islamic theme branding
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
