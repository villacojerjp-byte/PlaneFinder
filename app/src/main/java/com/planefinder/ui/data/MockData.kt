package com.planefinder.ui.data

object MockData {

    val homeAirport = Airport("SFO", "San Francisco", "San Francisco Intl")

    val flights = listOf(
        Flight(
            id = "1", callsign = "UAL238", flightNumber = "UA 238", airline = "United Airlines",
            airlineCode = "UA", aircraftType = "B789", aircraftName = "Boeing 787-9 Dreamliner",
            registration = "N24976",
            originCode = "SFO", originCity = "San Francisco", originName = "San Francisco Intl",
            destCode = "NRT", destCity = "Tokyo", destName = "Narita Intl",
            altitudeFt = 38000, groundSpeedKt = 514, headingDeg = 295f, verticalRateFpm = 0,
            squawk = "2174", progress = 0.42f, status = FlightStatus.EN_ROUTE,
            depTime = "11:25", arrTime = "15:10⁺¹", durationLabel = "11h 45m", distanceKm = 8267,
            x = 0.46f, y = 0.30f, speedFactor = 1.1f
        ),
        Flight(
            id = "2", callsign = "AAL19", flightNumber = "AA 19", airline = "American Airlines",
            airlineCode = "AA", aircraftType = "B77W", aircraftName = "Boeing 777-300ER",
            registration = "N729AN",
            originCode = "LAX", originCity = "Los Angeles", originName = "Los Angeles Intl",
            destCode = "LHR", destCity = "London", destName = "Heathrow",
            altitudeFt = 41000, groundSpeedKt = 548, headingDeg = 52f, verticalRateFpm = 0,
            squawk = "5521", progress = 0.61f, status = FlightStatus.EN_ROUTE,
            depTime = "14:05", arrTime = "08:30⁺¹", durationLabel = "10h 25m", distanceKm = 8780,
            x = 0.62f, y = 0.55f, speedFactor = 0.9f
        ),
        Flight(
            id = "3", callsign = "DLH441", flightNumber = "LH 441", airline = "Lufthansa",
            airlineCode = "LH", aircraftType = "A359", aircraftName = "Airbus A350-900",
            registration = "D-AIXP",
            originCode = "FRA", originCity = "Frankfurt", originName = "Frankfurt am Main",
            destCode = "IAD", destCity = "Washington", destName = "Dulles Intl",
            altitudeFt = 36000, groundSpeedKt = 491, headingDeg = 268f, verticalRateFpm = -200,
            squawk = "1043", progress = 0.74f, status = FlightStatus.EN_ROUTE,
            depTime = "10:15", arrTime = "13:05", durationLabel = "9h 50m", distanceKm = 6510,
            x = 0.30f, y = 0.42f, speedFactor = 1.0f
        ),
        Flight(
            id = "4", callsign = "UAE229", flightNumber = "EK 229", airline = "Emirates",
            airlineCode = "EK", aircraftType = "A388", aircraftName = "Airbus A380-800",
            registration = "A6-EVK",
            originCode = "DXB", originCity = "Dubai", originName = "Dubai Intl",
            destCode = "SFO", destCity = "San Francisco", destName = "San Francisco Intl",
            altitudeFt = 40000, groundSpeedKt = 532, headingDeg = 18f, verticalRateFpm = 0,
            squawk = "3320", progress = 0.88f, status = FlightStatus.APPROACHING,
            depTime = "09:30", arrTime = "13:55", durationLabel = "16h 25m", distanceKm = 13041,
            x = 0.52f, y = 0.66f, speedFactor = 1.2f
        ),
        Flight(
            id = "5", callsign = "SIA31", flightNumber = "SQ 31", airline = "Singapore Airlines",
            airlineCode = "SQ", aircraftType = "A359", aircraftName = "Airbus A350-900ULR",
            registration = "9V-SGE",
            originCode = "SIN", originCity = "Singapore", originName = "Changi",
            destCode = "SFO", destCity = "San Francisco", destName = "San Francisco Intl",
            altitudeFt = 35000, groundSpeedKt = 470, headingDeg = 75f, verticalRateFpm = 1200,
            squawk = "4615", progress = 0.20f, status = FlightStatus.EN_ROUTE,
            depTime = "08:50", arrTime = "08:20", durationLabel = "15h 30m", distanceKm = 13573,
            x = 0.18f, y = 0.60f, speedFactor = 0.85f
        ),
        Flight(
            id = "6", callsign = "QFA9", flightNumber = "QF 9", airline = "Qantas",
            airlineCode = "QF", aircraftType = "B789", aircraftName = "Boeing 787-9 Dreamliner",
            registration = "VH-ZNK",
            originCode = "PER", originCity = "Perth", originName = "Perth",
            destCode = "LHR", destCity = "London", destName = "Heathrow",
            altitudeFt = 43000, groundSpeedKt = 505, headingDeg = 315f, verticalRateFpm = 0,
            squawk = "6201", progress = 0.50f, status = FlightStatus.EN_ROUTE,
            depTime = "19:00", arrTime = "05:10⁺¹", durationLabel = "17h 20m", distanceKm = 14499,
            x = 0.70f, y = 0.34f, speedFactor = 1.05f
        ),
        Flight(
            id = "7", callsign = "AFR84", flightNumber = "AF 84", airline = "Air France",
            airlineCode = "AF", aircraftType = "B772", aircraftName = "Boeing 777-200ER",
            registration = "F-GSPM",
            originCode = "CDG", originCity = "Paris", originName = "Charles de Gaulle",
            destCode = "SFO", destCity = "San Francisco", destName = "San Francisco Intl",
            altitudeFt = 37000, groundSpeedKt = 488, headingDeg = 285f, verticalRateFpm = 0,
            squawk = "2530", progress = 0.33f, status = FlightStatus.EN_ROUTE,
            depTime = "10:40", arrTime = "13:25", durationLabel = "11h 45m", distanceKm = 8975,
            x = 0.40f, y = 0.48f, speedFactor = 0.95f
        ),
        Flight(
            id = "8", callsign = "ANA8", flightNumber = "NH 8", airline = "ANA",
            airlineCode = "NH", aircraftType = "B788", aircraftName = "Boeing 787-8 Dreamliner",
            registration = "JA814A",
            originCode = "HND", originCity = "Tokyo", originName = "Haneda",
            destCode = "SFO", destCity = "San Francisco", destName = "San Francisco Intl",
            altitudeFt = 39000, groundSpeedKt = 521, headingDeg = 60f, verticalRateFpm = 0,
            squawk = "1167", progress = 0.55f, status = FlightStatus.EN_ROUTE,
            depTime = "17:05", arrTime = "10:00", durationLabel = "9h 55m", distanceKm = 8231,
            x = 0.26f, y = 0.28f, speedFactor = 1.0f
        ),
        Flight(
            id = "9", callsign = "BAW286", flightNumber = "BA 286", airline = "British Airways",
            airlineCode = "BA", aircraftType = "B788", aircraftName = "Boeing 787-8 Dreamliner",
            registration = "G-ZBJG",
            originCode = "LHR", originCity = "London", originName = "Heathrow",
            destCode = "SFO", destCity = "San Francisco", destName = "San Francisco Intl",
            altitudeFt = 38000, groundSpeedKt = 497, headingDeg = 280f, verticalRateFpm = -400,
            squawk = "3712", progress = 0.80f, status = FlightStatus.APPROACHING,
            depTime = "15:20", arrTime = "18:15", durationLabel = "11h 25m", distanceKm = 8616,
            x = 0.58f, y = 0.40f, speedFactor = 1.0f
        ),
        Flight(
            id = "10", callsign = "KAL23", flightNumber = "KE 23", airline = "Korean Air",
            airlineCode = "KE", aircraftType = "B748", aircraftName = "Boeing 747-8I",
            registration = "HL7644",
            originCode = "ICN", originCity = "Seoul", originName = "Incheon",
            destCode = "LAX", destCity = "Los Angeles", destName = "Los Angeles Intl",
            altitudeFt = 34000, groundSpeedKt = 478, headingDeg = 70f, verticalRateFpm = 0,
            squawk = "5044", progress = 0.45f, status = FlightStatus.EN_ROUTE,
            depTime = "14:30", arrTime = "09:10", durationLabel = "11h 40m", distanceKm = 9598,
            x = 0.34f, y = 0.20f, speedFactor = 0.9f
        ),
        Flight(
            id = "11", callsign = "SWA1442", flightNumber = "WN 1442", airline = "Southwest",
            airlineCode = "WN", aircraftType = "B738", aircraftName = "Boeing 737-800",
            registration = "N8642E",
            originCode = "SFO", originCity = "San Francisco", originName = "San Francisco Intl",
            destCode = "LAS", destCity = "Las Vegas", destName = "Harry Reid Intl",
            altitudeFt = 28000, groundSpeedKt = 412, headingDeg = 125f, verticalRateFpm = 800,
            squawk = "4521", progress = 0.35f, status = FlightStatus.EN_ROUTE,
            depTime = "12:10", arrTime = "13:35", durationLabel = "1h 25m", distanceKm = 670,
            x = 0.55f, y = 0.72f, speedFactor = 1.4f
        ),
        Flight(
            id = "12", callsign = "DAL1567", flightNumber = "DL 1567", airline = "Delta",
            airlineCode = "DL", aircraftType = "A321", aircraftName = "Airbus A321neo",
            registration = "N501DN",
            originCode = "SEA", originCity = "Seattle", originName = "Seattle-Tacoma",
            destCode = "SFO", destCity = "San Francisco", destName = "San Francisco Intl",
            altitudeFt = 11000, groundSpeedKt = 320, headingDeg = 165f, verticalRateFpm = -1400,
            squawk = "2201", progress = 0.92f, status = FlightStatus.APPROACHING,
            depTime = "13:45", arrTime = "15:55", durationLabel = "2h 10m", distanceKm = 1093,
            x = 0.48f, y = 0.78f, speedFactor = 1.3f
        ),
    )

    val departures = listOf(
        BoardEntry("12:10", "WN 1442", "Southwest", "WN", "LAS", "Las Vegas", "1", "B12", FlightStatus.BOARDING),
        BoardEntry("12:35", "UA 1130", "United", "UA", "DEN", "Denver", "3", "F8", FlightStatus.SCHEDULED),
        BoardEntry("12:50", "AS 312", "Alaska", "AS", "SEA", "Seattle", "1", "C3", FlightStatus.SCHEDULED),
        BoardEntry("13:05", "DL 488", "Delta", "DL", "ATL", "Atlanta", "1", "A6", FlightStatus.DELAYED),
        BoardEntry("13:20", "AA 720", "American", "AA", "DFW", "Dallas", "2", "D14", FlightStatus.SCHEDULED),
        BoardEntry("13:25", "UA 238", "United", "UA", "NRT", "Tokyo", "3", "G92", FlightStatus.BOARDING),
        BoardEntry("13:40", "JL 1", "Japan Airlines", "JL", "HND", "Tokyo", "I", "A1", FlightStatus.SCHEDULED),
        BoardEntry("13:55", "B6 615", "JetBlue", "B6", "JFK", "New York", "1", "B5", FlightStatus.SCHEDULED),
        BoardEntry("14:10", "VS 42", "Virgin Atlantic", "VS", "LHR", "London", "I", "G3", FlightStatus.SCHEDULED),
        BoardEntry("14:30", "AC 758", "Air Canada", "AC", "YVR", "Vancouver", "I", "A11", FlightStatus.SCHEDULED),
    )

    val arrivals = listOf(
        BoardEntry("13:25", "AF 84", "Air France", "AF", "CDG", "Paris", "I", "A4", FlightStatus.APPROACHING),
        BoardEntry("13:55", "EK 229", "Emirates", "EK", "DXB", "Dubai", "I", "A8", FlightStatus.APPROACHING),
        BoardEntry("14:15", "WN 880", "Southwest", "WN", "PHX", "Phoenix", "1", "B20", FlightStatus.EN_ROUTE),
        BoardEntry("14:40", "UA 552", "United", "UA", "ORD", "Chicago", "3", "F12", FlightStatus.EN_ROUTE),
        BoardEntry("15:10", "AS 1190", "Alaska", "AS", "PDX", "Portland", "1", "C8", FlightStatus.EN_ROUTE),
        BoardEntry("15:55", "DL 1567", "Delta", "DL", "SEA", "Seattle", "1", "A2", FlightStatus.APPROACHING),
        BoardEntry("16:05", "NH 8", "ANA", "NH", "HND", "Tokyo", "I", "G98", FlightStatus.EN_ROUTE),
        BoardEntry("18:15", "BA 286", "British Airways", "BA", "LHR", "London", "I", "A6", FlightStatus.EN_ROUTE),
        BoardEntry("18:40", "QF 73", "Qantas", "QF", "SYD", "Sydney", "I", "G100", FlightStatus.SCHEDULED),
        BoardEntry("19:20", "SQ 31", "Singapore", "SQ", "SIN", "Singapore", "I", "A10", FlightStatus.SCHEDULED),
    )

    // Aircraft visible "overhead" in the AR view, with relative bearing & elevation.
    data class ArContact(
        val flight: Flight,
        val bearingDeg: Float,      // compass bearing where it appears
        val elevationDeg: Float,    // angle above horizon (0..90)
        val distanceMi: Float,
    )

    val arContacts = listOf(
        ArContact(flights[0], 28f, 44f, 6.2f),
        ArContact(flights[3], 95f, 22f, 11.8f),
        ArContact(flights[8], 150f, 61f, 3.4f),
        ArContact(flights[11], 310f, 15f, 18.0f),
        ArContact(flights[7], 200f, 38f, 8.7f),
    )
}
