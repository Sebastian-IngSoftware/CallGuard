package com.callguard.app.ui.theme

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Fondo de pantalla estilo CRT/terminal: finas líneas horizontales (scanlines)
 * apenas visibles sobre el negro, para dar textura sin romper el minimalismo.
 */
fun Modifier.terminalScanlines(
    spacing: Dp = 8.dp,
    lineColor: Color = NeuTertiary,
    alpha: Float = 0.05f
): Modifier = this.drawBehind {
    val step = spacing.toPx()
    var y = step
    while (y < size.height) {
        drawLine(
            color = lineColor.copy(alpha = alpha),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1f
        )
        y += step
    }
}

/**
 * Encabezado de pantalla estilo terminal:
 *
 *   > TITULO
 *   > subtitulo
 *   ----------
 */
@Composable
fun TerminalHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    val prompt = MaterialTheme.typography.titleLarge
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "",
                color = NeuAccent,
                style = prompt,
                fontWeight = FontWeight.Bold
            )
            Text(
                title,
                color = NeuText,
                style = prompt,
                fontWeight = FontWeight.Bold
            )
        }
        subtitle?.let {
            Spacer(Modifier.height(4.dp))
            Text(
                it,
                color = NeuTextMuted,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(12.dp))
        TerminalDivider()
    }
}

/**
 * Contenedor con borde cuadrado (sin esquinas redondeadas), tipo "ventana de terminal".
 */
@Composable
fun TerminalPanel(
    modifier: Modifier = Modifier,
    borderColor: Color = NeuBorder,
    background: Color = NeuBackground,
    contentPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(background)
            .border(1.dp, borderColor)
            .padding(contentPadding)
    ) {
        content()
    }
}

/**
 * Botón estilo terminal: borde cuadrado de acento, sin relleno en reposo;
 * al presionarlo (o estar seleccionado) se invierte: fondo de acento y texto del fondo.
 */
@Composable
fun TerminalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeuAccent,
    selected: Boolean = false,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isActive = isPressed || selected

    val background = if (isActive) accentColor else Color.Transparent
    val contentColor = if (isActive) NeuBackground else accentColor

    Box(
        modifier = modifier
            .background(background)
            .border(1.dp, accentColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

/**
 * Estado estilo terminal, ej: [ ACTIVO ] / [ ON ] / [ OFF ].
 */
@Composable
fun TerminalStatus(
    text: String,
    color: Color = NeuSuccess,
    modifier: Modifier = Modifier
) {
    Text(
        text = "[ $text ]",
        color = color,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
    )
}

/**
 * Separador de una línea.
 */
@Composable
fun TerminalDivider(
    modifier: Modifier = Modifier,
    color: Color = NeuBorder
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color)
    )
}

/**
 * Cursor parpadeante "_", típico de consola.
 */
@Composable
fun TerminalCursor(
    modifier: Modifier = Modifier,
    color: Color = NeuAccent
) {
    val transition = rememberInfiniteTransition(label = "cursor")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 550), RepeatMode.Reverse),
        label = "cursorAlpha"
    )
    Text(
        text = "_",
        color = color.copy(alpha = alpha),
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        fontFamily = FontFamily.Monospace
    )
}

/**
 * Fila de configuración tipo checkbox de terminal: [X] / [ ].
 */
@Composable
fun TerminalCheckRow(
    label: String,
    checked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = NeuText,
            modifier = Modifier.padding(end = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = if (checked) "[X]" else "[ ]",
            color = if (checked) NeuSuccess else NeuTextMuted,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Chip de opción estilo radio de terminal: (*) seleccionado / ( ) sin seleccionar.
 */
@Composable
fun TerminalRadioChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) NeuAccent else NeuTextMuted
    Box(
        modifier = modifier
            .background(if (selected) NeuAccent.copy(alpha = 0.1f) else Color.Transparent)
            .border(1.dp, if (selected) NeuAccent else NeuBorder)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (selected) "(*)" else "( )",
                color = color,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = label,
                color = color,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}