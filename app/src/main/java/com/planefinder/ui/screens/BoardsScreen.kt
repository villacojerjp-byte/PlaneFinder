package com.planefinder.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.outlined.ExpandMore
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.components.AirlineBadge
import com.planefinder.ui.components.StatusPill
import com.planefinder.ui.data.BoardEntry
import com.planefinder.ui.data.MockData
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.BgBase
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface1
import com.planefinder.ui.ui.theme.Surface2
import com.planefinder.ui.ui.theme.TextMuted
import com.planefinder.ui.ui.theme.TextPrimary
import com.planefinder.ui.ui.theme.TextSecondary

@Composable
fun BoardsScreen() {
    var departures by remember { mutableStateOf(true) }
    val airport = MockData.homeAirport
    val entries = if (departures) MockData.departures else MockData.arrivals

    Column(
        Modifier
            .fillMaxSize()
            .background(BgBase)
            .statusBarsPadding()
    ) {
        // Header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Accent.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) { Text(airport.code, color = Accent, fontSize = 14.sp, fontWeight = FontWeight.Black) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(airport.name, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Outlined.ExpandMore, "Change airport", tint = TextSecondary, modifier = Modifier.size(20.dp))
                }
                Text("Flight information display", color = TextSecondary, fontSize = 12.sp)
            }
        }

        // Segmented toggle
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Surface1)
                .border(1.dp, Outline, RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            SegTab("Departures", Icons.Filled.FlightTakeoff, departures, Modifier.weight(1f)) { departures = true }
            SegTab("Arrivals", Icons.Filled.FlightLand, !departures, Modifier.weight(1f)) { departures = false }
        }

        Spacer(Modifier.height(14.dp))

        // Column header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp)
        ) {
            Text("TIME", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(56.dp), letterSpacing = 1.sp)
            Text("FLIGHT", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), letterSpacing = 1.sp)
            Text(if (departures) "TO" else "FROM", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(entries) { e -> BoardRow(e, departures) }
        }
    }
}

@Composable
private fun SegTab(label: String, icon: ImageVector, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val alpha by animateFloatAsState(if (selected) 1f else 0f, label = "seg")
    Box(
        modifier
            .clip(RoundedCornerShape(11.dp))
            .background(Accent.copy(alpha = alpha))
            .clickable { onClick() }
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            Icon(icon, null, tint = if (selected) Color_onPrimary else TextSecondary, modifier = Modifier.size(18.dp))
            Text(label, color = if (selected) Color_onPrimary else TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun BoardRow(e: BoardEntry, departures: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface1)
            .border(1.dp, Outline, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            e.time,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(56.dp)
        )
        Spacer(Modifier.width(8.dp))
        AirlineBadge(e.airlineCode, 36)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("${e.cityCode} · ${e.city}", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(e.flightNumber, color = TextSecondary, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Text("Gate ${e.gate}", color = TextMuted, fontSize = 12.sp)
                Text("T${e.terminal}", color = TextMuted, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.width(8.dp))
        StatusPill(e.status)
    }
}
