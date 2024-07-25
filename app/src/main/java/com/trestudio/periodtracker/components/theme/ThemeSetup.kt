package com.trestudio.periodtracker.components.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object Theme {
    private val DarkColorScheme = darkColorScheme()
    private val LightColorScheme = lightColorScheme()
    private val Typography = Typography()

    @Composable
    fun Setup(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
        val isDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        val colors = when {
            isDynamicColor && dark -> dynamicDarkColorScheme(LocalContext.current)
            isDynamicColor && !dark -> dynamicLightColorScheme(LocalContext.current)
            dark -> DarkColorScheme
            else -> LightColorScheme
        }

        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            content = content
        )
    }
}