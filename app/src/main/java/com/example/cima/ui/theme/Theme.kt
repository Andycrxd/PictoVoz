package com.example.cima.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = CimaPrimary,
    onPrimary = CimaOnPrimary,
    secondary = CimaSecondary,
    background = CimaBackground,
    surface = CimaSurface,
    onSurface = CimaText,
    outline = CimaOutline,
    error = CimaWarning
)

@Composable
fun CimaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = CimaTypography,
        content = content
    )
}
