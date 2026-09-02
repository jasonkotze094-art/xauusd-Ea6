package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SamuraiDarkColorScheme = darkColorScheme(
  primary = CrimsonPrimary,
  onPrimary = Color.White,
  primaryContainer = CrimsonDeep,
  onPrimaryContainer = CrimsonGlow,
  secondary = CrimsonGlow,
  onSecondary = Color.White,
  secondaryContainer = DarkSurfaceElevated,
  onSecondaryContainer = TextPrimary,
  tertiary = AccentGold,
  onTertiary = Color.Black,
  background = DarkBackground,
  onBackground = TextPrimary,
  surface = DarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = DarkBorder,
  error = AccentRed,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = SamuraiDarkColorScheme,
    typography = Typography,
    content = content
  )
}

