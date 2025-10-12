package com.spundev.ringvolume.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF9C4049),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDADA),
    onPrimaryContainer = Color(0xFF7D2933),
    inversePrimary = Color(0xFFFFB3B6),
    secondary = Color(0xFF765657),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDADA),
    onSecondaryContainer = Color(0xFF5D3F40),
    tertiary = Color(0xFF76592F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDDB2),
    onTertiaryContainer = Color(0xFF5C421A),
    background = Color(0xFFF9F9F9),
    onBackground = Color(0xFF1B1B1B),
    surface = Color(0xFFF9F9F9),
    onSurface = Color(0xFF1B1B1B),
    surfaceVariant = Color(0xFFE2E2E2),
    onSurfaceVariant = Color(0xFF474747),
    surfaceTint = Color(0xFF9C4049),
    inverseSurface = Color(0xFF131313),
    inverseOnSurface = Color(0xFFE2E2E2),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    outline = Color(0xFF777777),
    outlineVariant = Color(0xFFC6C6C6),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFF9F9F9),
    surfaceDim = Color(0xFFDADADA),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF3F3F3),
    surfaceContainer = Color(0xFFEEEEEE),
    surfaceContainerHigh = Color(0xFFE8E8E8),
    surfaceContainerHighest = Color(0xFFE2E2E2)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB3B6),
    onPrimary = Color(0xFF5F121E),
    primaryContainer = Color(0xFF7D2933),
    onPrimaryContainer = Color(0xFFFFDADA),
    inversePrimary = Color(0xFF9C4049),
    secondary = Color(0xFFE6BDBD),
    onSecondary = Color(0xFF44292A),
    secondaryContainer = Color(0xFF5D3F40),
    onSecondaryContainer = Color(0xFFFFDADA),
    tertiary = Color(0xFFE6C08D),
    onTertiary = Color(0xFF432C06),
    tertiaryContainer = Color(0xFF5C421A),
    onTertiaryContainer = Color(0xFFFFDDB2),
    background = Color(0xFF131313),
    onBackground = Color(0xFFE2E2E2),
    surface = Color(0xFF131313),
    onSurface = Color(0xFFE2E2E2),
    surfaceVariant = Color(0xFF474747),
    onSurfaceVariant = Color(0xFFC6C6C6),
    surfaceTint = Color(0xFFFFB3B6),
    inverseSurface = Color(0xFFF9F9F9),
    inverseOnSurface = Color(0xFF1B1B1B),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    outline = Color(0xFF919191),
    outlineVariant = Color(0xFF474747),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF393939),
    surfaceDim = Color(0xFF131313),
    surfaceContainerLowest = Color(0xFF0E0E0E),
    surfaceContainerLow = Color(0xFF1B1B1B),
    surfaceContainer = Color(0xFF1F1F1F),
    surfaceContainerHigh = Color(0xFF2A2A2A),
    surfaceContainerHighest = Color(0xFF353535)
)

@Composable
fun RingVolumeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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