package com.planefinder.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.components.AirlineBadge
import com.planefinder.ui.data.MockData
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.Green
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface1
import com.planefinder.ui.ui.theme.Surface2
import com.planefinder.ui.ui.theme.TextPrimary
import com.planefinder.ui.ui.theme.TextSecondary
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun ArScreen(onBack: () -> Unit) {
    val contacts = remember { MockData.arContacts }
    var selected by remember { mutableStateOf<String?>(null) }
    val density = LocalDensity.current

    val t = rememberInfiniteTransition(label = "ar")
    // simulated heading drift (user slowly panning the phone)
    val heading by t.animateFloat(
        20f, 380f,
        infiniteRepeatable(tween(48000, easing = LinearEasing), RepeatMode.Restart),
        label = "heading"
    )
    val scan by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Reverse),
        label = "scan"
    )

    val fov = 80f

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF14314A), // upper sky
                        Color(0xFF1C4A63),
                        Color(0xFF24506B),
                        Color(0xFF0E1A24)  // toward ground
                    )
                )
            )
    ) {
        val wPx = with(density) { maxWidth.toPx() }
        val hPx = with(density) { maxHeight.toPx() }
        val horizonY = hPx * 0.62f

        // Sky grid + horizon
        Canvas(Modifier.fillMaxSize()) {
            // faint atmosphere haze near horizon
            drawRect(
                Brush.verticalGradient(
                    0.45f to Color.Transparent,
                    0.62f to Color(0x33FFFFFF),
                    0.63f to Color.Transparent
                )
            )
            // horizon line
            drawLine(Color(0x55FFFFFF), Offset(0f, horizonY), Offset(size.width, horizonY), 1.5f)
            // ground silhouette
            val ground = Path().apply {
                moveTo(0f, horizonY)
                lineTo(size.width * 0.2f, horizonY - 14f)
                lineTo(size.width * 0.45f, horizonY - 4f)
                lineTo(size.width * 0.7f, horizonY - 20f)
                lineTo(size.width, horizonY - 8f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(ground, Color(0xFF0B141C))
            // elevation guide arcs
            for (k in 1..3) {
                drawLine(
                    Color(0x18FFFFFF),
                    Offset(0f, horizonY - k * (horizonY / 4f)),
                    Offset(size.width, horizonY - k * (horizonY / 4f)),
                    1f
                )
            }
        }

        // Reticle
        Box(Modifier.align(Alignment.Center)) {
            Canvas(Modifier.size(90.dp)) {
                val c = Offset(size.width / 2, size.height / 2)
                drawCircle(Color(0x66FFFFFF), size.minDimension / 2 - 2, c, style = Stroke(2f))
                drawCircle(Accent.copy(alpha = scan * 0.6f), (size.minDimension / 2) * (0.4f + scan * 0.6f), c, style = Stroke(2f))
                val tick = 10f
                drawLine(Color.White, Offset(c.x, c.y - tick), Offset(c.x, c.y + tick), 2f)
                drawLine(Color.White, Offset(c.x - tick, c.y), Offset(c.x + tick, c.y), 2f)
            }
        }

        // Aircraft contacts
        contacts.forEach { contact ->
            var diff = ((contact.bearingDeg - heading + 540f) % 360f) - 180f
            if (abs(diff) <= fov / 2f) {
                val nx = 0.5f + (diff / fov)
                val ny = 1f - (contact.elevationDeg / 90f)
                val xPx = nx * wPx
                val yPx = (ny * horizonY).coerceIn(hPx * 0.08f, horizonY - 10f)
                val isSel = selected == contact.flight.id

                Box(
                    Modifier.offset {
                        IntOffset(
                            (xPx - with(density) { 90.dp.toPx() } / 2).roundToInt(),
                            yPx.roundToInt()
                        )
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // glyph
                        Canvas(Modifier.size(34.dp)) {
                            val cc = Offset(size.width / 2, size.height / 2)
                            if (isSel) drawCircle(Amber.copy(alpha = 0.25f), size.minDimension / 2, cc)
                            rotate(contact.bearingDeg - heading + 90f, cc) {
                                val s = 1.1f
                                val p = Path().apply {
                                    moveTo(cc.x, cc.y - 11f * s)
                                    lineTo(cc.x + 12f * s, cc.y + 5f * s)
                                    lineTo(cc.x + 3f * s, cc.y + 3f * s)
                                    lineTo(cc.x + 5f * s, cc.y + 11f * s)
                                    lineTo(cc.x, cc.y + 8f * s)
                                    lineTo(cc.x - 5f * s, cc.y + 11f * s)
                                    lineTo(cc.x - 3f * s, cc.y + 3f * s)
                                    lineTo(cc.x - 12f * s, cc.y + 5f * s)
                                    close()
                                }
                                drawPath(p, if (isSel) Amber else Color.White)
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        // label
                        Column(
                            Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Surface1.copy(alpha = 0.92f))
                                .border(1.dp, if (isSel) Amber else Outline, RoundedCornerShape(10.dp))
                                .clickable { selected = if (isSel) null else contact.flight.id }
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(contact.flight.flightNumber, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "${contact.flight.originCode} → ${contact.flight.destCode}",
                                color = TextSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace
                            )
                            Text(
                                "FL${contact.flight.altitudeFt / 100} • ${"%.1f".format(contact.distanceMi)} mi",
                                color = Accent, fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Top bar
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Surface1.copy(alpha = 0.7f))
                    .border(1.dp, Outline, CircleShape)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary) }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("AR Sky View", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(Green))
                    Text("Scanning the sky…", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        // Compass strip
        CompassStrip(
            heading = heading,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 70.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Bottom hint / selected info
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val sel = contacts.firstOrNull { it.flight.id == selected }
            if (sel != null) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface2)
                        .border(1.dp, Outline, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AirlineBadge(sel.flight.airlineCode, 44)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("${sel.flight.flightNumber} • ${sel.flight.airline}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("${sel.flight.originCity} → ${sel.flight.destCity}", color = TextSecondary, fontSize = 12.sp)
                        Text("${sel.flight.aircraftName}", color = Accent, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${sel.elevationDeg.toInt()}°", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("elevation", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface1.copy(alpha = 0.85f))
                        .border(1.dp, Outline, RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.FlightTakeoff, null, tint = Accent)
                    Text(
                        "Move your phone across the sky and tap an aircraft to identify it.",
                        color = TextSecondary, fontSize = 13.sp, modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompassStrip(heading: Float, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier
            .height(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0x55000000))
            .border(1.dp, Outline, RoundedCornerShape(10.dp))
    ) {
        val widthPx = with(density) { maxWidth.toPx() }
        val windowDeg = 120f
        // tick marks
        Canvas(Modifier.fillMaxSize()) {
            for (d in 0 until 360 step 15) {
                val rel = ((d - heading + 540f) % 360f) - 180f
                if (abs(rel) <= windowDeg / 2f) {
                    val x = size.width / 2 + rel / windowDeg * size.width
                    val major = d % 45 == 0
                    drawLine(
                        Color(if (major) 0xAAFFFFFFL else 0x55FFFFFFL),
                        Offset(x, size.height * (if (major) 0.45f else 0.65f)),
                        Offset(x, size.height),
                        if (major) 1.6f else 1f
                    )
                }
            }
            drawLine(Accent, Offset(size.width / 2, 0f), Offset(size.width / 2, size.height), 2f)
        }
        // labels
        val dirs = listOf("N" to 0f, "NE" to 45f, "E" to 90f, "SE" to 135f, "S" to 180f, "SW" to 225f, "W" to 270f, "NW" to 315f)
        dirs.forEach { (name, deg) ->
            val rel = ((deg - heading + 540f) % 360f) - 180f
            if (abs(rel) <= windowDeg / 2f - 8f) {
                Text(
                    name,
                    color = if (abs(rel) < 6f) Accent else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset { IntOffset((rel / windowDeg * widthPx).roundToInt(), with(density) { 3.dp.toPx() }.roundToInt()) }
                )
            }
        }
    }
}
