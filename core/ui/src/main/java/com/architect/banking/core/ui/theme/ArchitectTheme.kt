package com.architect.banking.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = ArchitectColors.NavyPrimary,
    onPrimary = ArchitectColors.White,
    primaryContainer = ArchitectColors.NavyTertiary,
    onPrimaryContainer = ArchitectColors.White,
    secondary = ArchitectColors.GoldAccent,
    onSecondary = ArchitectColors.NavyPrimary,
    secondaryContainer = ArchitectColors.GoldLight,
    onSecondaryContainer = ArchitectColors.NavyPrimary,
    background = ArchitectColors.OffWhite,
    onBackground = ArchitectColors.NavyPrimary,
    surface = ArchitectColors.CardBackground,
    onSurface = ArchitectColors.NavyPrimary,
    surfaceVariant = ArchitectColors.LightGray,
    onSurfaceVariant = ArchitectColors.DarkGray,
    error = ArchitectColors.Error,
    onError = ArchitectColors.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = ArchitectColors.GoldAccent,
    onPrimary = ArchitectColors.NavyPrimary,
    primaryContainer = ArchitectColors.NavySecondary,
    onPrimaryContainer = ArchitectColors.White,
    secondary = ArchitectColors.GoldLight,
    onSecondary = ArchitectColors.NavyPrimary,
    background = ArchitectColors.NavyPrimary,
    onBackground = ArchitectColors.White,
    surface = ArchitectColors.NavySecondary,
    onSurface = ArchitectColors.White,
    surfaceVariant = ArchitectColors.NavyTertiary,
    onSurfaceVariant = ArchitectColors.LightGray,
    error = ArchitectColors.Error,
    onError = ArchitectColors.White,
)

/**
 * Root theme composable wrapping [MaterialTheme] with Architect design tokens.
 * Must wrap the entire composable tree in [MainActivity].
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to system setting.
 * @param content The composable content to theme.
 */
@Composable
fun ArchitectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
