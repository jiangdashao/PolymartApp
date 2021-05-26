package me.rerere.polymartapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.Gray,
    primaryVariant = Color.Black,
    onPrimary = Color.White,
    secondary = Color.LightGray
)

private val LightColorPalette = lightColors(
    primary = POLYMART_COLOR,
    primaryVariant = POLYMART_COLOR_DARKER,
    secondary = POLYMART_COLOR_LIGHTER,
    onPrimary = Color.White,
    onSecondary = Color.White
)

@Composable
fun PolymartAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}