package com.holparb.moviefinder.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = PrimaryBackground,
    onBackground = SecondaryText,
    error = Error,
    onPrimary = PrimaryText,
    onSecondary = SecondaryText,
    surface = PrimaryBackground,
    onSurface = PrimaryText,
    primaryContainer = PrimaryBackground,
    secondaryContainer = SecondaryBackground
)

// App only uses dark theme so this has the same color scheme as dark
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = PrimaryBackground,
    onBackground = SecondaryText,
    error = Error,
    onPrimary = PrimaryText,
    onSecondary = SecondaryText,
    surface = PrimaryBackground,
    primaryContainer = PrimaryBackground,
    secondaryContainer = SecondaryBackground
)

@Composable
fun MovieFinderTheme(
    // For now app only has dark theme
    darkTheme: Boolean = true,
    // Dynamic color is not used for now
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
        content = content,
    )
}