package com.planefinder.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.data.FlightStatus
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface3

/** Brand-ish color per airline code so badges feel distinct. */
fun airlineColor(code: String): Color = when (code) {
    "UA" -> Color(0xFF2E5BBA)
    "AA" -> Color(0xFFC8102E)
    "DL" -> Color(0xFF9B1B30)
    "LH" -> Color(0xFF0A2240)
    "EK" -> Color(0xFFD71921)
    "SQ" -> Color(0xFF1A3A6B)
    "QF" -> Color(0xFFE0001B)
    "AF" -> Color(0xFF002157)
    "NH" -> Color(0xFF13448F)
    "BA" -> Color(0xFF1D3C6E)
    "KE" -> Color(0xFF1A4FA0)
    "WN" -> Color(0xFFF9B612)
    "AS" -> Color(0xFF01426A)
    "B6" -> Color(0xFF003876)
    "VS" -> Color(0xFFE10A0A)
    "AC" -> Color(0xFFD22630)
    "JL" -> Color(0xFFCE0E2D)
    else -> Color(0xFF2E5BBA)
}

@Composable
fun AirlineBadge(code: String, size: Int = 40) {
    val bg = airlineColor(code)
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.28f).dp))
            .background(
                Brush.linearGradient(
                    listOf(bg.copy(alpha = 0.95f), bg.copy(alpha = 0.65f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = code,
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = (size * 0.38f).sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun StatusPill(status: FlightStatus) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(CircleShape)
            .background(status.color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Box(
            Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(status.color)
        )
        Text(
            status.label,
            color = status.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun glassBorder() = BorderStroke(1.dp, Outline)

val GlassFill = Surface3.copy(alpha = 0.55f)
