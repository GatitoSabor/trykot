package com.example.lvlup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ⚠️ Esquema de colores para el tema CLARO
private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,          // Morado principal
    onPrimary = TextLight,            // Texto sobre morado principal (blanco)
    primaryContainer = LightPurple,   // Contenedor primario (morado claro)
    onPrimaryContainer = TextDark,    // Texto sobre contenedor primario (oscuro)
    secondary = AccentPurple,         // Acento (morado brillante)
    onSecondary = TextLight,          // Texto sobre acento (blanco)
    secondaryContainer = LightPurple,
    onSecondaryContainer = TextDark,
    tertiary = Pink40,
    onTertiary = Color.White,
    background = BackgroundWhite,     // Fondo blanco
    onBackground = TextDark,          // Texto sobre fondo (oscuro)
    surface = SurfaceWhite,           // Superficie blanca/grisácea
    onSurface = TextDark,             // Texto sobre superficie (oscuro)
    error = ErrorRed,                 // Rojo para errores
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF7B757F),
    surfaceVariant = Color(0xFFE7E0EB),
    onSurfaceVariant = Color(0xFF49454E),
    inverseSurface = Color(0xFF313034),
    inverseOnSurface = Color(0xFFF3F0F4),
    inversePrimary = Purple80,
    surfaceTint = PrimaryPurple,
    outlineVariant = Color(0xFFCAC4CF),
    scrim = Color(0xFF000000),
)

// ⚠️ Esquema de colores para el tema OSCURO (ajusta si quieres un tema oscuro morado)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.White,
    primaryContainer = PurpleGrey80,
    onPrimaryContainer = Color.White,
    secondary = Pink80,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Pink80,
    onTertiary = Color.White,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF958F98),
    surfaceVariant = Color(0xFF49454E),
    onSurfaceVariant = Color(0xFFCAC4CF),
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313034),
    inversePrimary = Purple40,
    surfaceTint = Purple80,
    outlineVariant = Color(0xFF49454E),
    scrim = Color(0xFF000000),
)


@Composable
fun LvlUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, // Podrías cambiar esto a 'false' si quieres forzar tus colores
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme // ⚠️ Usar nuestro esquema de color claro por defecto
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}