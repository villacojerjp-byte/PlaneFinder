package com.planefinder.ui.data

import androidx.compose.ui.graphics.Color
import com.planefinder.ui.ui.theme.Amber
import com.planefinder.ui.ui.theme.Green
import com.planefinder.ui.ui.theme.Red
import com.planefinder.ui.ui.theme.TextSecondary
import com.planefinder.ui.ui.theme.Yellow

enum class FlightStatus(val label: String, val color: Color) {
    SCHEDULED("Scheduled", TextSecondary),
    EN_ROUTE("En Route", Green),
    LANDED("Landed", TextSecondary),
    DELAYED("Delayed", Red),
    BOARDING("Boarding", Amber),
    APPROACHING("Approaching", Yellow),
}

/**
 * A live aircraft. Map position is normalized 0f..1f over the visible map area.
 * heading is degrees clockwise from north (0 = up).
 */
data class Flight(
    val id: String,
    val callsign: String,
    val flightNumber: String,
    val airline: String,
    val airlineCode: String,
    val aircraftType: String,
    val aircraftName: String,
    val registration: String,
    val originCode: String,
    val originCity: String,
    val originName: String,
    val destCode: String,
    val destCity: String,
    val destName: String,
    val altitudeFt: Int,
    val groundSpeedKt: Int,
    val headingDeg: Float,
    val verticalRateFpm: Int,
    val squawk: String,
    val progress: Float,        // 0f..1f along route
    val status: FlightStatus,
    val depTime: String,
    val arrTime: String,
    val durationLabel: String,
    val distanceKm: Int,
    val x: Float,               // normalized map x
    val y: Float,               // normalized map y
    val speedFactor: Float = 1f // relative animation speed
)

data class BoardEntry(
    val time: String,
    val flightNumber: String,
    val airline: String,
    val airlineCode: String,
    val cityCode: String,
    val city: String,
    val terminal: String,
    val gate: String,
    val status: FlightStatus,
)

data class Airport(
    val code: String,
    val city: String,
    val name: String,
)
