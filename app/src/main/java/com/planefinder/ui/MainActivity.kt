package com.planefinder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.screens.ArScreen
import com.planefinder.ui.screens.BoardsScreen
import com.planefinder.ui.screens.MapScreen
import com.planefinder.ui.screens.OnboardingScreen
import com.planefinder.ui.screens.PaywallScreen
import com.planefinder.ui.screens.ProfileScreen
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.BgBase
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.PlaneFinderTheme
import com.planefinder.ui.ui.theme.Surface1
import com.planefinder.ui.ui.theme.TextMuted

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaneFinderTheme {
                PlaneFinderApp()
            }
        }
    }
}

private enum class Tab(val label: String, val icon: ImageVector) {
    MAP("Map", Icons.Outlined.Public),
    BOARDS("Boards", Icons.Outlined.Schedule),
    AR("AR", Icons.Outlined.ViewInAr),
    PROFILE("Profile", Icons.Filled.Person),
}

@Composable
private fun PlaneFinderApp() {
    var onboarded by rememberSaveable { mutableStateOf(false) }

    if (!onboarded) {
        OnboardingScreen(onFinish = { onboarded = true })
        return
    }

    var tab by rememberSaveable { mutableStateOf(Tab.MAP) }
    var showAr by rememberSaveable { mutableStateOf(false) }
    var showPaywall by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showAr || showPaywall || tab != Tab.MAP) {
        when {
            showPaywall -> showPaywall = false
            showAr -> showAr = false
            else -> tab = Tab.MAP
        }
    }

    Box(Modifier.fillMaxSize().background(BgBase)) {
        // Main content + bottom bar
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxSize()) {
                when (tab) {
                    Tab.MAP -> MapScreen(onOpenAr = { showAr = true })
                    Tab.BOARDS -> BoardsScreen()
                    Tab.PROFILE -> ProfileScreen(onOpenPaywall = { showPaywall = true })
                    Tab.AR -> MapScreen(onOpenAr = { showAr = true }) // AR opens as overlay; keep map behind
                }
            }
            BottomBar(
                current = tab,
                onSelect = { selected ->
                    if (selected == Tab.AR) showAr = true else tab = selected
                }
            )
        }

        // AR full-screen overlay
        AnimatedVisibility(
            visible = showAr,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            ArScreen(onBack = { showAr = false })
        }

        // Paywall full-screen overlay
        AnimatedVisibility(
            visible = showPaywall,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            PaywallScreen(onClose = { showPaywall = false })
        }
    }
}

@Composable
private fun BottomBar(current: Tab, onSelect: (Tab) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Surface1)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Tab.entries.forEach { t ->
            BottomItem(
                tab = t,
                selected = current == t && t != Tab.AR,
                onClick = { onSelect(t) }
            )
        }
    }
}

@Composable
private fun BottomItem(tab: Tab, selected: Boolean, onClick: () -> Unit) {
    if (tab == Tab.AR) {
        // Prominent AR button
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Accent)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) { Icon(tab.icon, tab.label, tint = Color_onPrimary, modifier = Modifier.size(24.dp)) }
            Spacer(Modifier.height(3.dp))
            Text(tab.label, color = Accent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
        return
    }
    val tint = if (selected) Accent else TextMuted
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 6.dp)
    ) {
        Icon(tab.icon, tab.label, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(3.dp))
        Text(tab.label, color = tint, fontSize = 11.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}
