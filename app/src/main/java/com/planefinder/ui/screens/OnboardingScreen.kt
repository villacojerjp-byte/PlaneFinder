package com.planefinder.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.planefinder.ui.ui.theme.Accent
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.BgBase
import com.planefinder.ui.ui.theme.BgDeep
import com.planefinder.ui.ui.theme.Color_onPrimary
import com.planefinder.ui.ui.theme.Outline
import com.planefinder.ui.ui.theme.PlaneIdle
import com.planefinder.ui.ui.theme.TextSecondary
import com.planefinder.ui.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private data class Page(val title: String, val body: String, val kind: Int)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = listOf(
        Page("Track every flight, live", "Watch thousands of aircraft move across the globe in real time on a beautiful dark map.", 0),
        Page("Point at the sky", "Raise your phone and instantly identify any plane flying overhead with augmented reality.", 1),
        Page("Deep flight data", "Altitude, speed, routes, aircraft type, registration and live status — all at a glance.", 2),
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pages.lastIndex

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgDeep, BgBase)))
    ) {
        // Skip
        Text(
            "Skip",
            color = TextSecondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(20.dp)
                .clickable { onFinish() }
        )

        Column(Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val p = pages[page]
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OnboardingHero(p.kind)
                    Spacer(Modifier.height(48.dp))
                    Text(
                        p.title,
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        p.body,
                        color = TextSecondary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 28.dp)
                    .padding(bottom = 28.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { i ->
                        val active = i == pagerState.currentPage
                        val color by animateColorAsState(if (active) Accent else Outline, label = "dot")
                        Box(
                            Modifier
                                .padding(horizontal = 4.dp)
                                .height(7.dp)
                                .width(if (active) 22.dp else 7.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
                Button(
                    onClick = {
                        if (isLast) onFinish()
                        else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color_onPrimary)
                ) {
                    Text(if (isLast) "Get Started" else "Next", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun OnboardingHero(kind: Int) {
    val t = rememberInfiniteTransition(label = "hero")
    val rot by t.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Restart),
        label = "rot"
    )
    val pulse by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
        label = "pulse"
    )
    Box(Modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2f, size.height / 2f)
            val rMax = size.minDimension / 2f
            // glow
            drawCircle(
                Brush.radialGradient(
                    listOf(Accent.copy(alpha = 0.22f), Color.Transparent),
                    center = c, radius = rMax
                ),
                radius = rMax, center = c
            )
            when (kind) {
                0 -> { // radar with planes
                    for (k in 1..3) {
                        drawCircle(Accent.copy(alpha = 0.15f), rMax * k / 3.2f, c, style = Stroke(2f))
                    }
                    drawCircle(Accent.copy(alpha = (1f - pulse) * 0.4f), rMax * pulse, c, style = Stroke(2.5f))
                    val rad = Math.toRadians(rot.toDouble())
                    drawLine(
                        Accent.copy(alpha = 0.4f), c,
                        c + Offset((cos(rad) * rMax * 0.9f).toFloat(), (sin(rad) * rMax * 0.9f).toFloat()),
                        strokeWidth = 2.5f
                    )
                    listOf(40f to 0.55f, 160f to 0.4f, 280f to 0.7f).forEach { (ang, dist) ->
                        val r = Math.toRadians(ang.toDouble())
                        val pos = c + Offset((cos(r) * rMax * dist).toFloat(), (sin(r) * rMax * dist).toFloat())
                        plane(pos, ang + 90f, if (ang == 280f) Amber else PlaneIdle)
                    }
                }
                1 -> { // AR cone pointing up
                    val coneTop = Offset(c.x, c.y - rMax * 0.7f)
                    val path = Path().apply {
                        moveTo(c.x - rMax * 0.5f, c.y + rMax * 0.6f)
                        lineTo(coneTop.x, coneTop.y)
                        lineTo(c.x + rMax * 0.5f, c.y + rMax * 0.6f)
                    }
                    drawPath(path, Brush.verticalGradient(listOf(Accent.copy(alpha = 0.30f), Color.Transparent)))
                    drawPath(path, Accent.copy(alpha = 0.5f), style = Stroke(2f))
                    plane(Offset(c.x, c.y - rMax * 0.35f), 90f + pulse * 4f, Amber)
                    // crosshair
                    drawCircle(Accent, 4f, Offset(c.x, c.y + rMax * 0.2f))
                    drawCircle(Accent.copy(alpha = 0.4f), 12f, Offset(c.x, c.y + rMax * 0.2f), style = Stroke(2f))
                }
                else -> { // data rings
                    rotate(rot * 0.3f, c) {
                        for (k in 0..2) {
                            drawArc(
                                color = listOf(Accent, Amber, PlaneIdle)[k].copy(alpha = 0.7f),
                                startAngle = k * 100f,
                                sweepAngle = 80f,
                                useCenter = false,
                                topLeft = Offset(c.x - rMax * (0.4f + k * 0.18f), c.y - rMax * (0.4f + k * 0.18f)),
                                size = androidx.compose.ui.geometry.Size(rMax * 2 * (0.4f + k * 0.18f), rMax * 2 * (0.4f + k * 0.18f)),
                                style = Stroke(4f)
                            )
                        }
                    }
                    plane(c, 45f, Accent)
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.plane(center: Offset, heading: Float, color: Color) {
    rotate(heading, center) {
        val s = 1.6f
        val path = Path().apply {
            moveTo(center.x, center.y - 11f * s)
            lineTo(center.x + 3f * s, center.y - 2f * s)
            lineTo(center.x + 12f * s, center.y + 4f * s)
            lineTo(center.x + 12f * s, center.y + 6f * s)
            lineTo(center.x + 3f * s, center.y + 3f * s)
            lineTo(center.x + 3f * s, center.y + 9f * s)
            lineTo(center.x + 6f * s, center.y + 12f * s)
            lineTo(center.x, center.y + 10f * s)
            lineTo(center.x - 6f * s, center.y + 12f * s)
            lineTo(center.x - 3f * s, center.y + 9f * s)
            lineTo(center.x - 3f * s, center.y + 3f * s)
            lineTo(center.x - 12f * s, center.y + 6f * s)
            lineTo(center.x - 12f * s, center.y + 4f * s)
            lineTo(center.x - 3f * s, center.y - 2f * s)
            close()
        }
        drawPath(path, color)
    }
}
