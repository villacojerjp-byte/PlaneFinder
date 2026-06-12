// Ported from app/src/main/java/com/planefinder/ui/data/Models.kt
import { colors } from '../theme/colors'

export type FlightStatus =
  | 'SCHEDULED'
  | 'EN_ROUTE'
  | 'LANDED'
  | 'DELAYED'
  | 'BOARDING'
  | 'APPROACHING'

export const statusMeta: Record<FlightStatus, { label: string; color: string }> = {
  SCHEDULED: { label: 'Scheduled', color: colors.textSecondary },
  EN_ROUTE: { label: 'En Route', color: colors.green },
  LANDED: { label: 'Landed', color: colors.textSecondary },
  DELAYED: { label: 'Delayed', color: colors.red },
  BOARDING: { label: 'Boarding', color: colors.amber },
  APPROACHING: { label: 'Approaching', color: colors.yellow },
}

/**
 * A live aircraft. Map position is normalized 0..1 over the visible map area.
 * heading is degrees clockwise from north (0 = up).
 */
export interface Flight {
  id: string
  callsign: string
  flightNumber: string
  airline: string
  airlineCode: string
  aircraftType: string
  aircraftName: string
  registration: string
  originCode: string
  originCity: string
  originName: string
  destCode: string
  destCity: string
  destName: string
  altitudeFt: number
  groundSpeedKt: number
  headingDeg: number
  verticalRateFpm: number
  squawk: string
  progress: number // 0..1 along route
  status: FlightStatus
  depTime: string
  arrTime: string
  durationLabel: string
  distanceKm: number
  x: number // normalized map x
  y: number // normalized map y
  speedFactor: number // relative animation speed
}

export interface BoardEntry {
  time: string
  flightNumber: string
  airline: string
  airlineCode: string
  cityCode: string
  city: string
  terminal: string
  gate: string
  status: FlightStatus
}

export interface Airport {
  code: string
  city: string
  name: string
}

/** Aircraft visible "overhead" in the AR view, with relative bearing & elevation. */
export interface ArContact {
  flight: Flight
  bearingDeg: number // compass bearing where it appears
  elevationDeg: number // angle above horizon (0..90)
  distanceMi: number
}
