package com.przeman.sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Black,
    onPrimaryContainer = White,
    secondary = Green,
    onSecondary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
)

private val DarkColors = darkColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Black,
    onPrimaryContainer = White,
    secondary = Green,
    onSecondary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
)

@Composable
fun SampleTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(colorScheme = colors, content = content)
}
