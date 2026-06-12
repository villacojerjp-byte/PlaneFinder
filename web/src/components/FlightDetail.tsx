import { useEffect, useRef } from 'react'
import type { Flight } from '../data/models'
import { colors } from '../theme/colors'
import { Col, Row, Spacer } from './ui'
import { AirlineBadge } from './AirlineBadge'
import { StatusPill } from './StatusPill'
import {
  IcArrowOutward,
  IcBookmark,
  IcFlight,
  IcNavigation,
  IcSchedule,
  IcShare,
  IcSpeed,
  IcStraighten,
  IcSwapVert,
} from './icons'
import type { LucideIcon } from 'lucide-react'

const fmt = (n: number) => n.toLocaleString('en-US')

export function FlightDetail({ flight, onArView }: { flight: Flight; onArView: () => void }) {
  return (
    <Col style={{ padding: '0 20px 28px' }}>
      {/* Header */}
      <Row>
        <AirlineBadge code={flight.airlineCode} size={48} />
        <Spacer w={12} />
        <Col style={{ flex: 1 }}>
          <span style={{ color: colors.textPrimary, fontSize: 20, fontWeight: 700 }}>
            {flight.flightNumber}
          </span>
          <span style={{ color: colors.textSecondary, fontSize: 13 }}>{flight.airline}</span>
        </Col>
        <StatusPill status={flight.status} />
      </Row>

      <Spacer h={20} />

      {/* Route card */}
      <Col
        style={{
          borderRadius: 18,
          background: colors.surface2,
          border: `1px solid ${colors.outline}`,
          padding: 18,
        }}
      >
        <Row style={{ alignItems: 'flex-start' }}>
          <AirportEndpoint code={flight.originCode} city={flight.originCity} time={flight.depTime} align="flex-start" />
          <AirportEndpoint code={flight.destCode} city={flight.destCity} time={flight.arrTime} align="flex-end" />
        </Row>
        <Spacer h={14} />
        <RouteProgress progress={flight.progress} />
        <Spacer h={12} />
        <Row style={{ justifyContent: 'space-between' }}>
          <MiniMeta icon={IcSchedule} text={flight.durationLabel} />
          <MiniMeta icon={IcStraighten} text={`${fmt(flight.distanceKm)} km`} />
          <MiniMeta icon={IcNavigation} text={`${Math.trunc(flight.progress * 100)}% complete`} />
        </Row>
      </Col>

      <Spacer h={16} />

      {/* Live telemetry */}
      <SectionTitle text="Live telemetry" />
      <Spacer h={10} />
      <Row style={{ gap: 10, alignItems: 'stretch' }}>
        <StatTile icon={IcArrowOutward} label="Altitude" value={`${fmt(flight.altitudeFt)} ft`} />
        <StatTile icon={IcSpeed} label="Ground speed" value={`${flight.groundSpeedKt} kt`} />
      </Row>
      <Spacer h={10} />
      <Row style={{ gap: 10, alignItems: 'stretch' }}>
        <StatTile icon={IcNavigation} label="Heading" value={`${Math.trunc(flight.headingDeg)}°`} />
        <StatTile
          icon={IcSwapVert}
          label="Vertical"
          value={
            flight.verticalRateFpm === 0
              ? 'Level'
              : `${flight.verticalRateFpm > 0 ? '+' : ''}${flight.verticalRateFpm} fpm`
          }
        />
      </Row>

      <Spacer h={16} />

      {/* Aircraft */}
      <SectionTitle text="Aircraft" />
      <Spacer h={10} />
      <Col
        style={{
          borderRadius: 16,
          background: colors.surface2,
          border: `1px solid ${colors.outline}`,
          padding: 16,
        }}
      >
        <Row>
          <div
            style={{
              width: 46,
              height: 46,
              borderRadius: 12,
              background: colors.surface3,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <IcFlight size={24} color={colors.accent} />
          </div>
          <Spacer w={12} />
          <Col style={{ flex: 1 }}>
            <span style={{ color: colors.textPrimary, fontSize: 15, fontWeight: 600 }}>
              {flight.aircraftName}
            </span>
            <span style={{ color: colors.textSecondary, fontSize: 12 }}>
              Type {flight.aircraftType} • {flight.registration}
            </span>
          </Col>
        </Row>
        <Spacer h={14} />
        <Row style={{ gap: 20 }}>
          <KeyVal label="Callsign" value={flight.callsign} />
          <KeyVal label="Squawk" value={flight.squawk} />
          <KeyVal label="Reg" value={flight.registration} />
        </Row>
      </Col>

      <Spacer h={20} />

      {/* Actions */}
      <Row style={{ gap: 10, alignItems: 'stretch' }}>
        <button
          onClick={onArView}
          style={{
            flex: 1,
            height: 50,
            borderRadius: 14,
            background: colors.accent,
            color: colors.onPrimary,
            fontWeight: 700,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: 8,
          }}
        >
          <IcArrowOutward size={18} />
          <span>View in AR</span>
        </button>
        <IconButton icon={IcBookmark} />
        <IconButton icon={IcShare} />
      </Row>
    </Col>
  )
}

function AirportEndpoint({
  code,
  city,
  time,
  align,
}: {
  code: string
  city: string
  time: string
  align: 'flex-start' | 'flex-end'
}) {
  return (
    <Col style={{ flex: 1, alignItems: align, textAlign: align === 'flex-end' ? 'right' : 'left' }}>
      <span style={{ color: colors.textPrimary, fontSize: 30, fontWeight: 700, lineHeight: 1 }}>{code}</span>
      <span style={{ color: colors.textSecondary, fontSize: 12, marginTop: 4 }}>{city}</span>
      <Spacer h={4} />
      <span style={{ color: colors.accent, fontSize: 14, fontWeight: 600, fontFamily: 'var(--font-mono)' }}>
        {time}
      </span>
    </Col>
  )
}

function RouteProgress({ progress }: { progress: number }) {
  const ref = useRef<HTMLCanvasElement>(null)
  const wrapRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const canvas = ref.current!
    const wrap = wrapRef.current!
    const clamp = Math.max(0, Math.min(1, progress))
    const liftAt = (p: number) => 26 * Math.sin(p * Math.PI)

    const render = () => {
      const r = wrap.getBoundingClientRect()
      const w = r.width
      const h = 34
      const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
      canvas.width = Math.round(w * dpr)
      canvas.height = Math.round(h * dpr)
      canvas.style.width = `${w}px`
      canvas.style.height = `${h}px`
      const ctx = canvas.getContext('2d')!
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
      ctx.clearRect(0, 0, w, h)

      const y = h * 0.7
      const startX = 6
      const endX = w - 6
      const span = endX - startX
      const px = startX + span * clamp

      // dotted full path
      ctx.save()
      ctx.setLineDash([2, 9])
      ctx.lineCap = 'round'
      ctx.lineWidth = 3
      ctx.strokeStyle = colors.outline
      ctx.beginPath()
      ctx.moveTo(startX, y)
      ctx.lineTo(endX, y)
      ctx.stroke()
      ctx.restore()

      // flown arc (solid, curves up)
      const ctrlY = y - h * 0.55 * Math.max(Math.sin(clamp * Math.PI), 0.15)
      const midX = startX + (px - startX) / 2
      ctx.beginPath()
      ctx.moveTo(startX, y)
      ctx.quadraticCurveTo(midX, ctrlY, px, y - liftAt(clamp))
      ctx.strokeStyle = colors.accent
      ctx.lineWidth = 3.5
      ctx.lineCap = 'round'
      ctx.stroke()

      // endpoints
      for (const ex of [startX, endX]) {
        ctx.beginPath()
        ctx.arc(ex, y, 4, 0, Math.PI * 2)
        ctx.fillStyle = colors.textMuted
        ctx.fill()
      }

      // plane glyph at progress (rotated 90°)
      const planeY = y - liftAt(clamp)
      ctx.save()
      ctx.translate(px, planeY)
      ctx.rotate(Math.PI / 2)
      ctx.beginPath()
      ctx.moveTo(0, -9)
      ctx.lineTo(10, 5)
      ctx.lineTo(3, 4)
      ctx.lineTo(4, 10)
      ctx.lineTo(0, 8)
      ctx.lineTo(-4, 10)
      ctx.lineTo(-3, 4)
      ctx.lineTo(-10, 5)
      ctx.closePath()
      ctx.fillStyle = colors.amber
      ctx.fill()
      ctx.restore()
    }

    const ro = new ResizeObserver(render)
    ro.observe(wrap)
    render()
    return () => ro.disconnect()
  }, [progress])

  return (
    <div ref={wrapRef} style={{ width: '100%', height: 34 }}>
      <canvas ref={ref} />
    </div>
  )
}

function MiniMeta({ icon: Icon, text }: { icon: LucideIcon; text: string }) {
  return (
    <Row style={{ gap: 5 }}>
      <Icon size={15} color={colors.textSecondary} />
      <span style={{ color: colors.textSecondary, fontSize: 12 }}>{text}</span>
    </Row>
  )
}

function SectionTitle({ text }: { text: string }) {
  return (
    <span
      style={{
        color: colors.textMuted,
        fontSize: 12,
        fontWeight: 700,
        letterSpacing: 1.2,
        textTransform: 'uppercase',
      }}
    >
      {text}
    </span>
  )
}

function StatTile({ icon: Icon, label, value }: { icon: LucideIcon; label: string; value: string }) {
  return (
    <Col
      style={{
        flex: 1,
        borderRadius: 14,
        background: colors.surface2,
        border: `1px solid ${colors.outline}`,
        padding: 14,
      }}
    >
      <Icon size={18} color={colors.accent} />
      <Spacer h={8} />
      <span style={{ color: colors.textPrimary, fontSize: 18, fontWeight: 700 }}>{value}</span>
      <span style={{ color: colors.textSecondary, fontSize: 12 }}>{label}</span>
    </Col>
  )
}

function KeyVal({ label, value }: { label: string; value: string }) {
  return (
    <Col>
      <span style={{ color: colors.textMuted, fontSize: 10, letterSpacing: 0.8, textTransform: 'uppercase' }}>
        {label}
      </span>
      <Spacer h={2} />
      <span style={{ color: colors.textPrimary, fontSize: 14, fontWeight: 600, fontFamily: 'var(--font-mono)' }}>
        {value}
      </span>
    </Col>
  )
}

function IconButton({ icon: Icon }: { icon: LucideIcon }) {
  return (
    <button
      style={{
        height: 50,
        width: 50,
        borderRadius: 14,
        border: `1px solid ${colors.outline}`,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
      }}
    >
      <Icon size={20} color={colors.textPrimary} />
    </button>
  )
}
