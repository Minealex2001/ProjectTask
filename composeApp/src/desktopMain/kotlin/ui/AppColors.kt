package org.queststudios.projecttask.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme

object AppColors {
    val border = Color(0xFF6750A4) // Ejemplo, puedes personalizar
    // Puedes agregar más colores personalizados aquí
}

val CustomLightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFBFE),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF49454F),
    secondary = Color(0xFF625B71),
    scrim = Color(0xFF000000),
    // Agrega aquí todos los colores que uses en tu app
)
