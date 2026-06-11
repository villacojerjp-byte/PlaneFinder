package com.planefinder.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.data.Flight
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.MapBottom
import com.planefinder.ui.ui.theme.MapGrid
import com.planefinder.ui.ui.theme.MapLand
import com.planefinder.ui.ui.theme.MapLandEdge
import com.planefinder.ui.ui.theme.MapTop
import com.planefinder.ui.ui.theme.PlaneIdle
import com.planefinder.ui.ui.theme.PlaneTrail
import com.planefinder.ui.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

private class PlanePos(x: Float, y: Float, val heading: Float, val speed: Float) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
}

@Composable
fun PlaneMap(
    flights: List<Flight>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val positions = remember(flights) {
        mutableStateListOf<PlanePos>().apply {
            flights.forEach { add(PlanePos(it.x, it.y, it.headingDeg, it.speedFactor)) }
        }
    }

    LaunchedEffect(flights) {
        var last = 0L
        while (true) {
            withFrameNanos { now ->
                if (last != 0L) {
                    val dt = (now - last) / 1_000_000_000f
                    positions.forEach { p ->
                        val rad = Math.toRadians(p.heading.toDouble())
                        val v = 0.012f * p.speed * dt
                        p.x += sin(rad).toFloat() * v
                        p.y += -cos(rad).toFloat() * v
                        if (p.x < -0.05f) p.x = 1.05f
                        if (p.x > 1.05f) p.x = -0.05f
                        if (p.y < -0.05f) p.y = 1.05f
                        if (p.y > 1.05f) p.y = -0.05f
                    }
                }
                last = now
            }
        }
    }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val infinite = rememberInfiniteTransition(label = "map")
    val pulse by infinite.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    val sweep by infinite.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )

    fun screenOf(nx: Float, ny: Float): Offset {
        val c = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
        val base = Offset(nx * canvasSize.width, ny * canvasSize.height)
        return c + (base - c) * scale + offset
    }

    Box(
        modifier = modifier
            .background(MapBottom)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(1f, 4f)
                    var newOffset = offset + pan
                    val maxX = size.width * (newScale - 1f) / 2f + size.width * 0.15f
                    val maxY = size.height * (newScale - 1f) / 2f + size.height * 0.15f
                    newOffset = Offset(
                        newOffset.x.coerceIn(-maxX, maxX),
                        newOffset.y.coerceIn(-maxY, maxY)
                    )
                    scale = newScale
                    offset = newOffset
                }
            }
            .pointerInput(flights, scale, offset, canvasSize) {
                detectTapGestures { tap ->
                    var hit: String? = null
                    var best = Float.MAX_VALUE
                    flights.forEachIndexed { i, f ->
                        val p = positions.getOrNull(i) ?: return@forEachIndexed
                        val s = screenOf(p.x, p.y)
                        val d = hypot(s.x - tap.x, s.y - tap.y)
                        if (d < 70f && d < best) { best = d; hit = f.id }
                    }
                    onSelect(hit)
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { canvasSize = Size(it.width.toFloat(), it.height.toFloat()) }
        ) {
            drawRect(brush = Brush.verticalGradient(listOf(MapTop, MapBottom)))
            withTransform({
                translate(offset.x, offset.y)
                scale(scale, scale, pivot = Offset(size.width / 2f, size.height / 2f))
            }) {
                drawLandmasses()
                drawGraticule()
                drawHomeRings(pulse, sweep)
                flights.forEachIndexed { i, f ->
                    val p = positions.getOrNull(i) ?: return@forEachIndexed
                    drawTrail(p.x * size.width, p.y * size.height, f.headingDeg)
                }
                flights.forEachIndexed { i, f ->
                    val p = positions.getOrNull(i) ?: return@forEachIndexed
                    drawPlane(p.x * size.width, p.y * size.height, f.headingDeg, f.id == selectedId, pulse)
                }
            }
        }

        val selIndex = flights.indexOfFirst { it.id == selectedId }
        if (selIndex >= 0 && canvasSize != Size.Zero) {
            val f = flights[selIndex]
            positions.getOrNull(selIndex)?.let { p ->
                val s = screenOf(p.x, p.y)
                Box(
                    Modifier
                        .offset {
                            IntOffset(
                                s.x.toInt() + with(density) { 16.dp.roundToPx() },
                                s.y.toInt() - with(density) { 14.dp.roundToPx() }
                            )
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xF21A2230))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(f.flightNumber, color = Amber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("FL${f.altitudeFt / 100}", color = PlaneIdle, fontSize = 11.sp)
                        Text("${f.groundSpeedKt}kt", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ---- Canvas drawing helpers ----

private fun DrawScope.drawGraticule() {
    val step = size.width / 8f
    var x = 0f
    while (x <= size.width) {
        drawLine(MapGrid, Offset(x, 0f), Offset(x, size.height), 1f)
        x += step
    }
    val stepY = size.height / 12f
    var y = 0f
    while (y <= size.height) {
        drawLine(MapGrid, Offset(0f, y), Offset(size.width, y), 1f)
        y += stepY
    }
}

private fun DrawScope.drawLandmasses() {
    val w = size.width
    val h = size.height
    fun blob(points: List<Pair<Float, Float>>): Path {
        val path = Path()
        points.forEachIndexed { i, (px, py) ->
            if (i == 0) path.moveTo(px * w, py * h) else path.lineTo(px * w, py * h)
        }
        path.close()
        return path
    }
    val lands = listOf(
        blob(listOf(-0.05f to 0.10f, 0.18f to 0.05f, 0.30f to 0.16f, 0.26f to 0.30f, 0.34f to 0.40f, 0.22f to 0.52f, 0.10f to 0.48f, -0.05f to 0.55f)),
        blob(listOf(0.55f to 0.08f, 0.78f to 0.04f, 0.95f to 0.14f, 1.05f to 0.30f, 0.88f to 0.34f, 0.74f to 0.26f, 0.60f to 0.30f, 0.52f to 0.18f)),
        blob(listOf(0.62f to 0.62f, 0.82f to 0.58f, 0.92f to 0.72f, 0.80f to 0.92f, 0.64f to 0.96f, 0.56f to 0.80f)),
        blob(listOf(0.04f to 0.74f, 0.20f to 0.70f, 0.30f to 0.82f, 0.22f to 0.98f, 0.06f to 1.02f)),
    )
    lands.forEach {
        drawPath(it, MapLand)
        drawPath(it, MapLandEdge, style = Stroke(width = 1.2f))
    }
}

private fun DrawScope.drawHomeRings(pulse: Float, sweep: Float) {
    val center = Offset(size.width * 0.48f, size.height * 0.74f)
    val maxR = size.minDimension * 0.30f
    for (k in 1..3) {
        drawCircle(Accent.copy(alpha = 0.10f), maxR * k / 3f, center, style = Stroke(width = 1f))
    }
    drawCircle(Accent.copy(alpha = (1f - pulse) * 0.35f), maxR * pulse, center, style = Stroke(width = 2f))
    val rad = Math.toRadians(sweep.toDouble())
    drawLine(
        Accent.copy(alpha = 0.25f), center,
        center + Offset((cos(rad) * maxR).toFloat(), (sin(rad) * maxR).toFloat()),
        strokeWidth = 2f
    )
    drawCircle(Accent, 5f, center)
    drawCircle(Accent.copy(alpha = 0.25f), 11f, center)
}

private fun DrawScope.drawTrail(cx: Float, cy: Float, heading: Float) {
    val rad = Math.toRadians(heading.toDouble())
    val dx = sin(rad).toFloat()
    val dy = -cos(rad).toFloat()
    val len = 46f
    drawLine(
        PlaneTrail,
        Offset(cx - dx * len, cy - dy * len),
        Offset(cx, cy),
        strokeWidth = 2.2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 5f), 0f)
    )
}

private fun DrawScope.drawPlane(cx: Float, cy: Float, heading: Float, selected: Boolean, pulse: Float) {
    val color = if (selected) Amber else PlaneIdle
    val s = if (selected) 1.25f else 1f
    if (selected) {
        drawCircle(Amber.copy(alpha = (1f - pulse) * 0.5f), 10f + pulse * 22f, Offset(cx, cy))
        drawCircle(Amber.copy(alpha = 0.18f), 16f, Offset(cx, cy))
    }
    rotate(degrees = heading, pivot = Offset(cx, cy)) {
        val path = Path().apply {
            moveTo(cx, cy - 11f * s)
            lineTo(cx + 3f * s, cy - 2f * s)
            lineTo(cx + 12f * s, cy + 4f * s)
            lineTo(cx + 12f * s, cy + 6f * s)
            lineTo(cx + 3f * s, cy + 3f * s)
            lineTo(cx + 3f * s, cy + 9f * s)
            lineTo(cx + 6f * s, cy + 12f * s)
            lineTo(cx + 6f * s, cy + 13.5f * s)
            lineTo(cx, cy + 11f * s)
            lineTo(cx - 6f * s, cy + 13.5f * s)
            lineTo(cx - 6f * s, cy + 12f * s)
            lineTo(cx - 3f * s, cy + 9f * s)
            lineTo(cx - 3f * s, cy + 3f * s)
            lineTo(cx - 12f * s, cy + 6f * s)
            lineTo(cx - 12f * s, cy + 4f * s)
            lineTo(cx - 3f * s, cy - 2f * s)
            close()
        }
        drawPath(path, color)
    }
}
