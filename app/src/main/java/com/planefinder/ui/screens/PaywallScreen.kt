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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.BgBase
import com.planefinder.ui.ui.theme.BgDeep
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.Green
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.Surface1
import com.planefinder.ui.ui.theme.Surface2
import com.planefinder.ui.ui.theme.TextMuted
import com.planefinder.ui.ui.theme.TextPrimary
import com.planefinder.ui.ui.theme.TextSecondary

private data class Perk(val icon: ImageVector, val title: String, val sub: String)

@Composable
fun PaywallScreen(onClose: () -> Unit) {
    var yearly by remember { mutableStateOf(true) }
    val perks = listOf(
        Perk(Icons.Outlined.ViewInAr, "Augmented reality", "Identify any aircraft overhead"),
        Perk(Icons.Outlined.Public, "3D & cockpit view", "Fly along in immersive 3D"),
        Perk(Icons.Outlined.FilterAlt, "Unlimited filters", "Airline, altitude, type & more"),
        Perk(Icons.Outlined.Layers, "Weather & airspace", "Live overlays on the map"),
        Perk(Icons.Outlined.Notifications, "Flight alerts", "Status, gate & delay notifications"),
        Perk(Icons.Outlined.Bolt, "No ads, ever", "A clean, focused experience"),
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgDeep, BgBase)))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 22.dp)
                .padding(bottom = 160.dp)
        ) {
            // Close
            Box(Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.CenterEnd) {
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Surface1)
                        .clickable { onClose() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Close, "Close", tint = TextSecondary, modifier = Modifier.size(20.dp)) }
            }

            Spacer(Modifier.height(8.dp))

            // Hero badge
            Box(
                Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(Accent, Amber)))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Outlined.Bolt, null, tint = Color_onPrimary, modifier = Modifier.size(44.dp)) }

            Spacer(Modifier.height(20.dp))
            Text(
                "Plane Finder Pro",
                color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Unlock the full sky. Everything you need to track, identify and follow flights.",
                color = TextSecondary, fontSize = 14.sp, lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            perks.forEach { perk ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Surface2),
                        contentAlignment = Alignment.Center
                    ) { Icon(perk.icon, null, tint = Accent, modifier = Modifier.size(20.dp)) }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(perk.title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(perk.sub, color = TextSecondary, fontSize = 12.sp)
                    }
                    Icon(Icons.Filled.Check, null, tint = Green, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.height(20.dp))

            // Plans
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PlanCard(
                    title = "Yearly", price = "$39.99", per = "per year",
                    note = "Just $3.33/mo", badge = "SAVE 40%",
                    selected = yearly, modifier = Modifier.weight(1f)
                ) { yearly = true }
                PlanCard(
                    title = "Monthly", price = "$5.99", per = "per month",
                    note = "Billed monthly", badge = null,
                    selected = !yearly, modifier = Modifier.weight(1f)
                ) { yearly = false }
            }
        }

        // Sticky CTA
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, BgBase, BgBase)))
                .navigationBarsPadding()
                .padding(horizontal = 22.dp)
                .padding(top = 24.dp, bottom = 18.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color_onPrimary)
            ) {
                Text(
                    "Start 7-day free trial",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "Then ${if (yearly) "$39.99/year" else "$5.99/month"} • Cancel anytime  •  Restore",
                color = TextMuted, fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    per: String,
    note: String,
    badge: String?,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) Accent.copy(alpha = 0.12f) else Surface1)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Accent else Outline,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            if (badge != null) {
                Box(
                    Modifier.clip(CircleShape).background(Amber).padding(horizontal = 8.dp, vertical = 3.dp)
                ) { Text(badge, color = Color_onPrimary, fontSize = 9.sp, fontWeight = FontWeight.Black) }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(price, color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(per, color = TextSecondary, fontSize = 12.sp)
        Spacer(Modifier.height(6.dp))
        Text(note, color = if (selected) Accent else TextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
