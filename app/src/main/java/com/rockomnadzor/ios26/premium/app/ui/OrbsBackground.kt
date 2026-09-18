package com.rockomnadzor.ios26.premium.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

private data class Orb(
    val cxFraction: Float,
    val cyFraction: Float,
    val radiusFraction: Float,
    val baseGray: Float
)

// Позиции сфер в процентах от размера экрана — легко подвинуть под любое разрешение
private val orbs = listOf(
    Orb(cxFraction = 0.75f, cyFraction = 0.05f, radiusFraction = 0.55f, baseGray = 0.10f),
    Orb(cxFraction = 0.10f, cyFraction = 0.22f, radiusFraction = 0.42f, baseGray = 0.07f),
    Orb(cxFraction = 0.65f, cyFraction = 0.62f, radiusFraction = 0.50f, baseGray = 0.06f),
    Orb(cxFraction = 0.15f, cyFraction = 0.95f, radiusFraction = 0.40f, baseGray = 0.09f)
)

@Composable
fun OrbsBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // Базовый чистый чёрный фон
        drawRect(color = Color.Black)

        orbs.forEach { orb ->
            val center = Offset(size.width * orb.cxFraction, size.height * orb.cyFraction)
            val radius = size.minDimension * orb.radiusFraction

            drawOrbBody(center, radius, orb.baseGray)
            drawRimLight(center, radius)
        }
    }
}

// Тело сферы: объёмный радиальный градиент, светлее у "источника света" (верх-лево от центра)
private fun DrawScope.drawOrbBody(center: Offset, radius: Float, baseGray: Float) {
    val lightOffset = Offset(center.x - radius * 0.35f, center.y - radius * 0.35f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = baseGray + 0.06f),
                Color.Black.copy(alpha = 1f - baseGray * 0.3f),
                Color.Black
            ),
            center = lightOffset,
            radius = radius * 1.3f
        ),
        radius = radius,
        center = center
    )
}

// Тонкая светящаяся обводка по краю сферы — эффект, видимый на пересечениях на скрине
private fun DrawScope.drawRimLight(center: Offset, radius: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent),
            center = center,
            radius = radius * 1.02f
        ),
        radius = radius,
        center = center,
        style = Stroke(width = 2.5f)
    )
}
