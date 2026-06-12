import { useEffect, useRef, useState } from 'react'
import { arContacts } from '../data/mockData'
import { colors, withAlpha } from '../theme/colors'
import { Col, Row, Spacer } from '../components/ui'
import { AirlineBadge } from '../components/AirlineBadge'
import { IcArrowBack, IcFlightTakeoff } from '../components/icons'

const FOV = 80

function useElapsed(): number {
  const [, setTick] = useState(0)
  const start = useRef(0)
  useEffect(() => {
    let raf = 0
    const loop = (now: number) => {
      if (start.current === 0) start.current = now
      setTick(now - start.current)
      raf = requestAnimationFrame(loop)
    }
    raf = requestAnimationFrame(loop)
    return () => cancelAnimationFrame(raf)
  }, [])
  return start.current === 0 ? 0 : performance.now() - start.current
}

function useSize<T extends HTMLElement>() {
  const ref = useRef<T>(null)
  const [size, setSize] = useState({ w: 0, h: 0 })
  useEffect(() => {
    const el = ref.current
    if (!el) return
    const ro = new ResizeObserver(() => {
      const r = el.getBoundingClientRect()
      setSize({ w: r.width, h: r.height })
    })
    ro.observe(el)
    return () => ro.disconnect()
  }, [])
  return [ref, size] as const
}

export function ArScreen({ onBack }: { onBack: () => void }) {
  const [selected, setSelected] = useState<string | null>(null)
  const elapsed = useElapsed()
  const heading = 20 + ((elapsed % 48000) / 48000) * 360

  const [wrapRef, size] = useSize<HTMLDivElement>()
  const horizonY = size.h * 0.62
  const sel = arContacts.find((c) => c.flight.id === selected) ?? null

  return (
    <div
      ref={wrapRef}
      style={{
        position: 'absolute',
        inset: 0,
        overflow: 'hidden',
        background: `linear-gradient(180deg, #14314A, #1C4A63, #24506B, #0E1A24)`,
      }}
    >
      <SkyBackground w={size.w} h={size.h} horizonY={horizonY} />

      {/* Reticle */}
      <div style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)' }}>
        <Reticle />
      </div>

      {/* Aircraft contacts */}
      {size.w > 0 &&
        arContacts.map((contact) => {
          let diff = ((contact.bearingDeg - heading + 540) % 360) - 180
          if (Math.abs(diff) > FOV / 2) return null
          const nx = 0.5 + diff / FOV
          const ny = 1 - contact.elevationDeg / 90
          const xPx = nx * size.w
          const yPx = Math.max(size.h * 0.08, Math.min(horizonY - 10, ny * horizonY))
          const isSel = selected === contact.flight.id
          return (
            <div
              key={contact.flight.id}
              style={{ position: 'absolute', left: xPx, top: yPx, transform: 'translateX(-50%)' }}
            >
              <Col style={{ alignItems: 'center' }}>
                <ContactGlyph rotation={contact.bearingDeg - heading + 90} selected={isSel} />
                <Spacer h={4} />
                <Col
                  onClick={() => setSelected(isSel ? null : contact.flight.id)}
                  style={{
                    alignItems: 'center',
                    borderRadius: 10,
                    background: withAlpha(colors.surface1, 0.92),
                    border: `1px solid ${isSel ? colors.amber : colors.outline}`,
                    padding: '7px 10px',
                    cursor: 'pointer',
                    whiteSpace: 'nowrap',
                  }}
                >
                  <span style={{ color: colors.textPrimary, fontSize: 13, fontWeight: 700 }}>
                    {contact.flight.flightNumber}
                  </span>
                  <span style={{ color: colors.textSecondary, fontSize: 11, fontFamily: 'var(--font-mono)' }}>
                    {contact.flight.originCode} → {contact.flight.destCode}
                  </span>
                  <span style={{ color: colors.accent, fontSize: 10 }}>
                    FL{Math.round(contact.flight.altitudeFt / 100)} • {contact.distanceMi.toFixed(1)} mi
                  </span>
                </Col>
              </Col>
            </div>
          )
        })}

      {/* Top bar */}
      <Row style={{ position: 'absolute', top: 0, left: 0, right: 0, padding: '10px 16px', paddingTop: 'calc(10px + var(--safe-top))' }}>
        <button
          onClick={onBack}
          style={{
            width: 44,
            height: 44,
            borderRadius: 999,
            background: withAlpha(colors.surface1, 0.7),
            border: `1px solid ${colors.outline}`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <IcArrowBack size={22} color={colors.textPrimary} />
        </button>
        <Spacer w={12} />
        <Col>
          <span style={{ color: colors.textPrimary, fontSize: 17, fontWeight: 700 }}>AR Sky View</span>
          <Row style={{ gap: 6 }}>
            <div style={{ width: 7, height: 7, borderRadius: 999, background: colors.green }} />
            <span style={{ color: colors.textSecondary, fontSize: 12 }}>Scanning the sky…</span>
          </Row>
        </Col>
      </Row>

      {/* Compass strip */}
      <div
        style={{
          position: 'absolute',
          top: 'calc(70px + var(--safe-top))',
          left: 16,
          right: 16,
        }}
      >
        <CompassStrip heading={heading} />
      </div>

      {/* Bottom hint / selected info */}
      <div
        style={{
          position: 'absolute',
          left: 0,
          right: 0,
          bottom: 0,
          padding: 16,
          paddingBottom: 'calc(16px + var(--safe-bottom))',
        }}
      >
        {sel ? (
          <Row
            style={{
              borderRadius: 16,
              background: colors.surface2,
              border: `1px solid ${colors.outline}`,
              padding: 14,
            }}
          >
            <AirlineBadge code={sel.flight.airlineCode} size={44} />
            <Spacer w={12} />
            <Col style={{ flex: 1, minWidth: 0 }}>
              <span style={{ color: colors.textPrimary, fontSize: 14, fontWeight: 600 }}>
                {sel.flight.flightNumber} • {sel.flight.airline}
              </span>
              <span style={{ color: colors.textSecondary, fontSize: 12 }}>
                {sel.flight.originCity} → {sel.flight.destCity}
              </span>
              <span style={{ color: colors.accent, fontSize: 12 }}>{sel.flight.aircraftName}</span>
            </Col>
            <Col style={{ alignItems: 'flex-end' }}>
              <span style={{ color: colors.textPrimary, fontSize: 18, fontWeight: 700 }}>
                {Math.trunc(sel.elevationDeg)}°
              </span>
              <span style={{ color: colors.textSecondary, fontSize: 11 }}>elevation</span>
            </Col>
          </Row>
        ) : (
          <Row
            style={{
              borderRadius: 16,
              background: withAlpha(colors.surface1, 0.85),
              border: `1px solid ${colors.outline}`,
              padding: 14,
              gap: 10,
            }}
          >
            <IcFlightTakeoff size={22} color={colors.accent} />
            <span style={{ color: colors.textSecondary, fontSize: 13, flex: 1 }}>
              Move your phone across the sky and tap an aircraft to identify it.
            </span>
          </Row>
        )}
      </div>
    </div>
  )
}

function planePath(s: number): string {
  const c = 17
  const pts: [number, number][] = [
    [0, -11], [12, 5], [3, 3], [5, 11], [0, 8], [-5, 11], [-3, 3], [-12, 5],
  ]
  return 'M' + pts.map(([x, y]) => `${c + x * s},${c + y * s}`).join(' L') + ' Z'
}

function ContactGlyph({ rotation, selected }: { rotation: number; selected: boolean }) {
  return (
    <svg width={34} height={34} viewBox="0 0 34 34" style={{ overflow: 'visible' }}>
      {selected && <circle cx={17} cy={17} r={17} fill={withAlpha(colors.amber, 0.25)} />}
      <path
        d={planePath(1.1)}
        fill={selected ? colors.amber : '#FFFFFF'}
        transform={`rotate(${rotation} 17 17)`}
      />
    </svg>
  )
}

function Reticle() {
  const ref = useRef<HTMLCanvasElement>(null)
  useEffect(() => {
    const canvas = ref.current!
    const ctx = canvas.getContext('2d')!
    const size = 90
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
    canvas.width = size * dpr
    canvas.height = size * dpr
    canvas.style.width = `${size}px`
    canvas.style.height = `${size}px`
    let raf = 0
    const loop = (now: number) => {
      const phase = (now % 4400) / 2200
      const scan = phase <= 1 ? phase : 2 - phase
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
      ctx.clearRect(0, 0, size, size)
      const c = size / 2
      ctx.beginPath()
      ctx.arc(c, c, size / 2 - 2, 0, Math.PI * 2)
      ctx.strokeStyle = withAlpha('#FFFFFF', 0.4)
      ctx.lineWidth = 2
      ctx.stroke()
      ctx.beginPath()
      ctx.arc(c, c, (size / 2) * (0.4 + scan * 0.6), 0, Math.PI * 2)
      ctx.strokeStyle = withAlpha(colors.accent, scan * 0.6)
      ctx.lineWidth = 2
      ctx.stroke()
      const tick = 10
      ctx.strokeStyle = '#FFFFFF'
      ctx.lineWidth = 2
      ctx.beginPath()
      ctx.moveTo(c, c - tick)
      ctx.lineTo(c, c + tick)
      ctx.moveTo(c - tick, c)
      ctx.lineTo(c + tick, c)
      ctx.stroke()
      raf = requestAnimationFrame(loop)
    }
    raf = requestAnimationFrame(loop)
    return () => cancelAnimationFrame(raf)
  }, [])
  return <canvas ref={ref} />
}

const DIRS: [string, number][] = [
  ['N', 0], ['NE', 45], ['E', 90], ['SE', 135], ['S', 180], ['SW', 225], ['W', 270], ['NW', 315],
]

function CompassStrip({ heading }: { heading: number }) {
  const [ref, size] = useSize<HTMLDivElement>()
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const windowDeg = 120

  useEffect(() => {
    const canvas = canvasRef.current
    if (!canvas || size.w === 0) return
    const ctx = canvas.getContext('2d')!
    const w = size.w
    const h = 34
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
    canvas.width = Math.round(w * dpr)
    canvas.height = Math.round(h * dpr)
    canvas.style.width = `${w}px`
    canvas.style.height = `${h}px`
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
    ctx.clearRect(0, 0, w, h)
    for (let d = 0; d < 360; d += 15) {
      const rel = ((d - heading + 540) % 360) - 180
      if (Math.abs(rel) <= windowDeg / 2) {
        const x = w / 2 + (rel / windowDeg) * w
        const major = d % 45 === 0
        ctx.strokeStyle = withAlpha('#FFFFFF', major ? 0.67 : 0.33)
        ctx.lineWidth = major ? 1.6 : 1
        ctx.beginPath()
        ctx.moveTo(x, h * (major ? 0.45 : 0.65))
        ctx.lineTo(x, h)
        ctx.stroke()
      }
    }
    ctx.strokeStyle = colors.accent
    ctx.lineWidth = 2
    ctx.beginPath()
    ctx.moveTo(w / 2, 0)
    ctx.lineTo(w / 2, h)
    ctx.stroke()
  }, [heading, size.w])

  return (
    <div
      ref={ref}
      style={{
        position: 'relative',
        height: 34,
        borderRadius: 10,
        background: 'rgba(0,0,0,0.33)',
        border: `1px solid ${colors.outline}`,
        overflow: 'hidden',
      }}
    >
      <canvas ref={canvasRef} style={{ position: 'absolute', inset: 0 }} />
      {DIRS.map(([name, deg]) => {
        const rel = ((deg - heading + 540) % 360) - 180
        if (Math.abs(rel) > windowDeg / 2 - 8) return null
        return (
          <span
            key={name}
            style={{
              position: 'absolute',
              top: 3,
              left: '50%',
              transform: `translateX(calc(-50% + ${(rel / windowDeg) * size.w}px))`,
              color: Math.abs(rel) < 6 ? colors.accent : '#FFFFFF',
              fontSize: 12,
              fontWeight: 700,
            }}
          >
            {name}
          </span>
        )
      })}
    </div>
  )
}

function SkyBackground({ w, h, horizonY }: { w: number; h: number; horizonY: number }) {
  const ref = useRef<HTMLCanvasElement>(null)
  useEffect(() => {
    const canvas = ref.current
    if (!canvas || w === 0 || h === 0) return
    const ctx = canvas.getContext('2d')!
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
    canvas.width = Math.round(w * dpr)
    canvas.height = Math.round(h * dpr)
    canvas.style.width = `${w}px`
    canvas.style.height = `${h}px`
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
    ctx.clearRect(0, 0, w, h)

    // atmosphere haze near horizon
    const haze = ctx.createLinearGradient(0, 0, 0, h)
    haze.addColorStop(0.45, 'transparent')
    haze.addColorStop(0.62, withAlpha('#FFFFFF', 0.2))
    haze.addColorStop(0.63, 'transparent')
    ctx.fillStyle = haze
    ctx.fillRect(0, 0, w, h)

    // horizon line
    ctx.strokeStyle = withAlpha('#FFFFFF', 0.33)
    ctx.lineWidth = 1.5
    ctx.beginPath()
    ctx.moveTo(0, horizonY)
    ctx.lineTo(w, horizonY)
    ctx.stroke()

    // ground silhouette
    ctx.beginPath()
    ctx.moveTo(0, horizonY)
    ctx.lineTo(w * 0.2, horizonY - 14)
    ctx.lineTo(w * 0.45, horizonY - 4)
    ctx.lineTo(w * 0.7, horizonY - 20)
    ctx.lineTo(w, horizonY - 8)
    ctx.lineTo(w, h)
    ctx.lineTo(0, h)
    ctx.closePath()
    ctx.fillStyle = '#0B141C'
    ctx.fill()

    // elevation guide lines
    for (let k = 1; k <= 3; k++) {
      const y = horizonY - k * (horizonY / 4)
      ctx.strokeStyle = withAlpha('#FFFFFF', 0.094)
      ctx.lineWidth = 1
      ctx.beginPath()
      ctx.moveTo(0, y)
      ctx.lineTo(w, y)
      ctx.stroke()
    }
  }, [w, h, horizonY])

  return <canvas ref={ref} style={{ position: 'absolute', inset: 0 }} />
}
