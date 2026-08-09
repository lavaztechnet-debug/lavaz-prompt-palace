package com.lavaz.promptpalace.ui.theme
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
private val DarkColorScheme = darkColorScheme(primary = LavaPrimary, secondary = GlowingOrange, tertiary = AccentBlue, background = DarkCanvas, surface = DarkSurface)
private val LightColorScheme = lightColorScheme(primary = LavaPrimary, secondary = GlowingOrange, tertiary = AccentBlue, background = LightCanvas, surface = LightSurface)
@Composable fun LavazPromptPalaceTheme(darkTheme:Boolean = isSystemInDarkTheme(), content:@Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val systemUiController = rememberSystemUiController()
        SideEffect { systemUiController.setStatusBarColor(if (darkTheme) DarkCanvas else LightCanvas, darkIcons = !darkTheme) }
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
