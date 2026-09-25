package com.callguard.app.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modifier con el look de neumorfismo: dibuja una sombra clara (arriba-izquierda)
 * y una sombra oscura (abajo-derecha) alrededor de una superficie del color de fondo,
 * simulando que la forma sobresale (modo "raised") o está hundida (modo "inset",
 * pensado para estados presionados/seleccionados, donde se invierten las sombras).
 *
 * Basado en la técnica clásica de BlurMaskFilter sobre un Canvas nativo, que no
 * requiere API 31+ (a diferencia de RenderEffect/graphicsLayer blur).
 */
fun Modifier.neumorphic(
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 10.dp,
    surfaceColor: Color = NeuSurface,
    lightShadowColor: Color = NeuLightShadow,
    darkShadowColor: Color = NeuDarkShadow,
    inset: Boolean = false
): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawBehind {
        val cornerPx = cornerRadius.toPx()
        val blurPx = elevation.toPx()
        val offsetPx = elevation.toPx() * 0.6f

        val darkOffset = if (inset) -offsetPx else offsetPx
        val lightOffset = if (inset) offsetPx else -offsetPx

        drawIntoCanvas { canvas ->
            val dark = android.graphics.Paint().apply {
                color = darkShadowColor.copy(alpha = 0.55f).toArgb()
                isAntiAlias = true
                maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
            canvas.nativeCanvas.drawRoundRect(
                darkOffset, darkOffset,
                size.width + darkOffset, size.height + darkOffset,
                cornerPx, cornerPx, dark
            )

            val light = android.graphics.Paint().apply {
                color = lightShadowColor.toArgb()
                isAntiAlias = true
                maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
            canvas.nativeCanvas.drawRoundRect(
                lightOffset, lightOffset,
                size.width + lightOffset, size.height + lightOffset,
                cornerPx, cornerPx, light
            )

            // Superficie plana encima, tapa el centro y deja visible solo el halo del borde.
            val fill = android.graphics.Paint().apply {
                color = surfaceColor.toArgb()
                isAntiAlias = true
            }
            canvas.nativeCanvas.drawRoundRect(
                0f, 0f, size.width, size.height, cornerPx, cornerPx, fill
            )
        }
    }
    .clip(RoundedCornerShape(cornerRadius))

/**
 * Tarjeta con relieve neumórfico "raised". Reemplaza a Card en las pantallas de CallGuard.
 */
@Composable
fun NeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 10.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.neumorphic(cornerRadius = cornerRadius, elevation = elevation)
    ) {
        content()
    }
}

/**
 * Botón neumórfico: se ve "elevado" en reposo y "hundido" (inset) mientras se lo presiona,
 * dando el feedback táctil típico del estilo. El color de acento (turquesa) se usa
 * para el texto/ícono cuando está resaltado.
 */
@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 8.dp,
    contentColor: Color = NeuAccent,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .neumorphic(
                cornerRadius = cornerRadius,
                elevation = elevation,
                inset = isPressed
            )
            .clip(RoundedCornerShape(cornerRadius))
            .clickableNoRipple(interactionSource = interactionSource, onClick = onClick),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.runtime.CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

/**
 * Contenedor "inset" (hundido), pensado para inputs de texto: da la sensación
 * de una ranura tallada en la superficie, en vez de una tarjeta que sobresale.
 */
@Composable
fun NeumorphicInset(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp,
    elevation: Dp = 6.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.neumorphic(cornerRadius = cornerRadius, elevation = elevation, inset = true)
    ) {
        content()
    }
}

private fun Modifier.clickableNoRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
): Modifier = this.clickable(
    interactionSource = interactionSource,
    indication = null,
    onClick = onClick
)