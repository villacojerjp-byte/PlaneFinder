package com.planefinder.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.components.FlightDetailContent
import com.planefinder.ui.components.PlaneMap
import com.planefinder.ui.data.MockData
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.Green
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface1
import com.planefinder.ui.ui.theme.Surface2
import com.planefinder.ui.ui.theme.TextMuted
import com.planefinder.ui.ui.theme.TextPrimary
import com.planefinder.ui.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onOpenAr: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val flights = remember { MockData.flights }
    var selectedId by remember { mutableStateOf<String?>(null) }
    var activeFilter by remember { mutableStateOf("All") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val filters = listOf("All", "En Route", "Arrivals", "Departures", "Emergency")

    Box(modifier.fillMaxSize()) {
        PlaneMap(
            flights = flights,
            selectedId = selectedId,
            onSelect = { selectedId = it },
            modifier = Modifier.fillMaxSize()
        )

        // Top controls
        Column(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            SearchBar()
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { f ->
                    FilterChip(f, f == activeFilter) { activeFilter = f }
                }
            }
        }

        // Right-side floating tools
        Column(
            Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MapTool(Icons.Outlined.Layers) {}
            MapTool(Icons.Outlined.Map) {}
            MapTool(Icons.Outlined.MyLocation) {}
            MapTool(Icons.Outlined.ViewInAr, highlight = true) { onOpenAr() }
        }

        // Bottom live status pill
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
        ) {
            LiveStatus(flights.size)
        }

        if (selectedId != null) {
            val flight = flights.first { it.id == selectedId }
            ModalBottomSheet(
                onDismissRequest = { selectedId = null },
                sheetState = sheetState,
                containerColor = Surface1,
                contentColor = TextPrimary,
                dragHandle = {
                    Box(Modifier.fillMaxWidth().padding(top = 12.dp), contentAlignment = Alignment.Center) {
                        Box(
                            Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Outline)
                        )
                    }
                }
            ) {
                FlightDetailContent(
                    flight = flight,
                    onArView = { onOpenAr() }
                )
            }
        }
    }
}

@Composable
private fun SearchBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Surface1.copy(alpha = 0.92f))
            .border(1.dp, Outline, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text("Search flights, airports, routes…", color = TextMuted, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Box(
            Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Accent.copy(alpha = 0.16f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.Tune, "Filters", tint = Accent, modifier = Modifier.size(18.dp)) }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Accent else Surface1.copy(alpha = 0.9f)
    val fg = if (selected) Color_onPrimary else TextSecondary
    Box(
        Modifier
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, if (selected) Color.Transparent else Outline, CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Text(label, color = fg, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MapTool(icon: ImageVector, highlight: Boolean = false, onClick: () -> Unit) {
    Box(
        Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (highlight) Accent else Surface1.copy(alpha = 0.92f))
            .border(1.dp, if (highlight) Color.Transparent else Outline, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = if (highlight) Color_onPrimary else TextPrimary, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun LiveStatus(count: Int) {
    Row(
        Modifier
            .clip(CircleShape)
            .background(Surface2.copy(alpha = 0.95f))
            .border(1.dp, Outline, CircleShape)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(Green))
        Text("$count aircraft tracked", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text("• live", color = Green, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}
