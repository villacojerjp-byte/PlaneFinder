package com.planefinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.data.Flight
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface2
import com.planefinder.ui.ui.theme.Surface3
import com.planefinder.ui.ui.theme.TextMuted
import com.planefinder.ui.ui.theme.TextPrimary
import com.planefinder.ui.ui.theme.TextSecondary
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun FlightDetailContent(
    flight: Flight,
    onArView: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            AirlineBadge(flight.airlineCode, 48)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(flight.flightNumber, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(flight.airline, color = TextSecondary, fontSize = 13.sp)
            }
            StatusPill(flight.status)
        }

        Spacer(Modifier.height(20.dp))

        // Route card
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Surface2)
                .border(1.dp, Outline, RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                AirportEndpoint(flight.originCode, flight.originCity, flight.depTime, Alignment.Start, Modifier.weight(1f))
                AirportEndpoint(flight.destCode, flight.destCity, flight.arrTime, Alignment.End, Modifier.weight(1f))
            }
            Spacer(Modifier.height(14.dp))
            RouteProgress(flight.progress, Modifier.fillMaxWidth().height(34.dp))
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniMeta(Icons.Outlined.Schedule, flight.durationLabel)
                MiniMeta(Icons.Outlined.Straighten, "${"%,d".format(flight.distanceKm)} km")
                MiniMeta(Icons.Outlined.Navigation, "${flight.progress.times(100).toInt()}% complete")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Live telemetry
        SectionTitle("Live telemetry")
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile(Icons.Outlined.ArrowOutward, "Altitude", "${"%,d".format(flight.altitudeFt)} ft", Modifier.weight(1f))
            StatTile(Icons.Outlined.Speed, "Ground speed", "${flight.groundSpeedKt} kt", Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatTile(Icons.Outlined.Navigation, "Heading", "${flight.headingDeg.toInt()}°", Modifier.weight(1f))
            StatTile(
                Icons.Outlined.SwapVert,
                "Vertical",
                if (flight.verticalRateFpm == 0) "Level"
                else "${if (flight.verticalRateFpm > 0) "+" else ""}${flight.verticalRateFpm} fpm",
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Aircraft
        SectionTitle("Aircraft")
        Spacer(Modifier.height(10.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Surface2)
                .border(1.dp, Outline, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Surface3),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Flight, null, tint = Accent, modifier = Modifier.size(24.dp)) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(flight.aircraftName, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text("Type ${flight.aircraftType} • ${flight.registration}", color = TextSecondary, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                KeyVal("Callsign", flight.callsign)
                KeyVal("Squawk", flight.squawk)
                KeyVal("Reg", flight.registration)
            }
        }

        Spacer(Modifier.height(20.dp))

        // Actions
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onArView,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color_onPrimary)
            ) {
                Icon(Icons.Outlined.ArrowOutward, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("View in AR", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Outline)
            ) { Icon(Icons.Outlined.Bookmark, "Follow", tint = TextPrimary, modifier = Modifier.size(20.dp)) }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Outline)
            ) { Icon(Icons.Filled.Share, "Share", tint = TextPrimary, modifier = Modifier.size(20.dp)) }
        }
    }
}

@Composable
private fun AirportEndpoint(code: String, city: String, time: String, align: Alignment.Horizontal, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = align) {
        Text(code, color = TextPrimary, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif)
        Text(city, color = TextSecondary, fontSize = 12.sp)
        Spacer(Modifier.height(4.dp))
        Text(time, color = Accent, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun RouteProgress(progress: Float, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val y = size.height * 0.7f
        val startX = 6f
        val endX = size.width - 6f
        val span = endX - startX
        val px = startX + span * progress.coerceIn(0f, 1f)
        // dotted full path
        drawLine(
            Outline, Offset(startX, y), Offset(endX, y),
            strokeWidth = 3f, cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 9f))
        )
        // flown arc (solid, slight curve up)
        val arc = Path().apply {
            moveTo(startX, y)
            val midX = startX + (px - startX) / 2f
            quadraticBezierTo(midX, y - size.height * 0.55f * sin((progress.coerceIn(0f, 1f)) * PI.toFloat()).coerceAtLeast(0.15f), px, y - liftAt(progress))
        }
        drawPath(arc, Accent, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
        // endpoints
        drawCircle(TextMuted, 4f, Offset(startX, y))
        drawCircle(TextMuted, 4f, Offset(endX, y))
        // plane glyph at progress
        val planeY = y - liftAt(progress)
        rotate(degrees = 90f, pivot = Offset(px, planeY)) {
            val p = Path().apply {
                moveTo(px, planeY - 9f)
                lineTo(px + 10f, planeY + 5f)
                lineTo(px + 3f, planeY + 4f)
                lineTo(px + 4f, planeY + 10f)
                lineTo(px, planeY + 8f)
                lineTo(px - 4f, planeY + 10f)
                lineTo(px - 3f, planeY + 4f)
                lineTo(px - 10f, planeY + 5f)
                close()
            }
            drawPath(p, Amber)
        }
    }
}

private fun liftAt(progress: Float): Float = 26f * sin(progress.coerceIn(0f, 1f) * PI.toFloat())

@Composable
private fun MiniMeta(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(15.dp))
        Text(text, color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text.uppercase(),
        color = TextMuted,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )
}

@Composable
private fun StatTile(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Surface2)
            .border(1.dp, Outline, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Icon(icon, null, tint = Accent, modifier = Modifier.size(18.dp))
        Spacer(Modifier.height(8.dp))
        Text(value, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun KeyVal(label: String, value: String) {
    Column {
        Text(label.uppercase(), color = TextMuted, fontSize = 10.sp, letterSpacing = 0.8.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
    }
}
