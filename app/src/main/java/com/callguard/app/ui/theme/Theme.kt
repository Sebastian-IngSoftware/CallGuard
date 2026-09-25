package com.callguard.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// CallGuard siempre usa el esquema oscuro con la paleta neumórfica,
// independientemente del tema del sistema: es parte de la identidad visual de la app.
private val CallGuardColors = darkColorScheme(
    primary = NeuAccent,
    onPrimary = NeuPrimary,
    secondary = NeuSecondary,
    onSecondary = NeuText,
    tertiary = NeuTertiary,
    onTertiary = NeuText,
    background = NeuBackground,
    onBackground = NeuText,
    surface = NeuSurface,
    onSurface = NeuText,
    surfaceVariant = NeuSurface,
    onSurfaceVariant = NeuTextMuted,
    outline = NeuBorder,
    error = NeuSuccess // no usamos error real acá, pero queda consistente si algo lo pide
)

@Composable
fun CallGuardTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CallGuardColors,
        content = content
    )
}
