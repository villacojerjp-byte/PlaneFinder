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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.BgBase
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface1
import com.planefinder.ui.ui.theme.Surface2
import com.planefinder.ui.ui.theme.TextMuted
import com.planefinder.ui.ui.theme.TextPrimary
import com.planefinder.ui.ui.theme.TextSecondary

@Composable
fun ProfileScreen(onOpenPaywall: () -> Unit) {
    var notifications by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(true) }

    Column(
        Modifier
            .fillMaxSize()
            .background(BgBase)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
    ) {
        Spacer(Modifier.height(12.dp))
        Text("Profile", color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        // Account card
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Surface1)
                .border(1.dp, Outline, RoundedCornerShape(18.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(54.dp).clip(CircleShape).background(Surface2),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Person, null, tint = TextSecondary, modifier = Modifier.size(28.dp)) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("Spotter", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text("Free plan", color = TextSecondary, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Pro banner
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.linearGradient(listOf(Accent.copy(alpha = 0.22f), Amber.copy(alpha = 0.18f))))
                .border(1.dp, Accent.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                .clickable { onOpenPaywall() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(44.dp).clip(RoundedCornerShape(13.dp))
                    .background(Brush.linearGradient(listOf(Accent, Amber))),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.Bolt, null, tint = Color_onPrimary) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("Upgrade to Pro", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("AR, 3D, alerts & more", color = TextSecondary, fontSize = 12.sp)
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = TextSecondary)
        }

        Spacer(Modifier.height(22.dp))
        GroupLabel("Preferences")
        SettingsGroup {
            ToggleRow(Icons.Outlined.Notifications, "Flight alerts", notifications) { notifications = it }
            Divider()
            ToggleRow(Icons.Outlined.DarkMode, "Dark map", darkMode) { darkMode = it }
            Divider()
            NavRow(Icons.Outlined.Map, "Map style", "Satellite")
            Divider()
            NavRow(Icons.Outlined.Speed, "Units", "Knots · Feet")
        }

        Spacer(Modifier.height(18.dp))
        GroupLabel("About")
        SettingsGroup {
            NavRow(Icons.Outlined.Star, "Rate the app", null)
            Divider()
            NavRow(Icons.Outlined.Info, "About Plane Finder", "v1.0")
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "UI/UX concept • mock data",
            color = TextMuted, fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun GroupLabel(text: String) {
    Text(
        text.uppercase(),
        color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsGroup(content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Surface1)
            .border(1.dp, Outline, RoundedCornerShape(18.dp))
    ) { content() }
}

@Composable
private fun Divider() {
    Box(Modifier.fillMaxWidth().height(1.dp).padding(horizontal = 16.dp).background(Outline))
}

@Composable
private fun ToggleRow(icon: ImageVector, label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Accent, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, color = TextPrimary, fontSize = 15.sp, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color_onPrimary,
                checkedTrackColor = Accent,
                uncheckedTrackColor = Surface2,
                uncheckedBorderColor = Outline
            )
        )
    }
}

@Composable
private fun NavRow(icon: ImageVector, label: String, value: String?) {
    Row(
        Modifier.fillMaxWidth().clickable {}.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Accent, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, color = TextPrimary, fontSize = 15.sp, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.Outlined.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(20.dp))
    }
}
