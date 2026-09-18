package com.rockomnadzor.ios26.premium.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin

// Squircle — суперэллипс, как у иконок Apple
class SquircleShape(private val exponent: Float = 4.5f) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path()
        val w = size.width; val h = size.height
        val steps = 72
        for (i in 0..steps) {
            val t = (i / steps.toFloat()) * (2 * Math.PI)
            val cosT = cos(t).toFloat(); val sinT = sin(t).toFloat()
            val x = (w / 2f) * sign(cosT) * abs(cosT).pow(2f / exponent)
            val y = (h / 2f) * sign(sinT) * abs(sinT).pow(2f / exponent)
            if (i == 0) path.moveTo(w / 2f + x, h / 2f + y) else path.lineTo(w / 2f + x, h / 2f + y)
        }
        path.close()
        return Outline.Generic(path)
    }
}

val GlassShape = SquircleShape()

// Базовая "soft liquid glass" поверхность: настоящий backdrop-blur через Haze
@Composable
fun GlassSurface(
    hazeState: HazeState,
    shape: Shape = GlassShape,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .hazeChild(state = hazeState, shape = shape) {
                blurRadius = 26.dp
                tint = Color.White.copy(alpha = 0.08f)
            }
            .background(
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.10f), Color.White.copy(alpha = 0.02f))
                ),
                shape
            )
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(listOf(Color.White.copy(alpha = 0.35f), Color.Transparent))
                ),
                shape
            ),
        content = content
    )
}

@Composable
fun GlassIconContainer(
    hazeState: HazeState,
    icon: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    GlassSurface(hazeState = hazeState, modifier = modifier.size(60.dp)) {
        Box(modifier = Modifier.padding(10.dp).fillMaxSize()) { icon() }
    }
}

@Composable
fun GlassFolder(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    GlassSurface(hazeState = hazeState, modifier = modifier.size(72.dp), content = content)
}

@Composable
fun DynamicIslandCapsule(hazeState: HazeState, modifier: Modifier = Modifier) {
    GlassSurface(
        hazeState = hazeState,
        shape = RoundedCornerShape(50),
        modifier = modifier.width(120.dp).height(36.dp)
    ) {}
}
