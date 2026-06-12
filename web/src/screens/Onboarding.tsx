import { useEffect, useRef, useState } from 'react'
import { colors, withAlpha } from '../theme/colors'
import { Col, Spacer } from '../components/ui'
import { IcArrowForward } from '../components/icons'

interface Page {
  title: string
  body: string
  kind: number
}

const PAGES: Page[] = [
  { title: 'Track every flight, live', body: 'Watch thousands of aircraft move across the globe in real time on a beautiful dark map.', kind: 0 },
  { title: 'Point at the sky', body: 'Raise your phone and instantly identify any plane flying overhead with augmented reality.', kind: 1 },
  { title: 'Deep flight data', body: 'Altitude, speed, routes, aircraft type, registration and live status — all at a glance.', kind: 2 },
]

export function Onboarding({ onFinish }: { onFinish: () => void }) {
  const [page, setPage] = useState(0)
  const scrollRef = useRef<HTMLDivElement>(null)
  const isLast = page === PAGES.length - 1

  const onScroll = () => {
    const el = scrollRef.current!
    setPage(Math.round(el.scrollLeft / el.clientWidth))
  }

  const next = () => {
    if (isLast) {
      onFinish()
      return
    }
    const el = scrollRef.current!
    el.scrollTo({ left: (page + 1) * el.clientWidth, behavior: 'smooth' })
  }

  return (
    <div
      style={{
        position: 'absolute',
        inset: 0,
        background: `linear-gradient(180deg, ${colors.bgDeep}, ${colors.bgBase})`,
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      {/* Skip */}
      <button
        onClick={onFinish}
        style={{
          position: 'absolute',
          top: 'calc(20px + var(--safe-top))',
          right: 20,
          zIndex: 2,
          color: colors.textSecondary,
          fontSize: 15,
          fontWeight: 600,
        }}
      >
        Skip
      </button>

      {/* Pager */}
      <div
        ref={scrollRef}
        onScroll={onScroll}
        className="no-scrollbar"
        style={{
          flex: 1,
          display: 'flex',
          overflowX: 'auto',
          scrollSnapType: 'x mandatory',
        }}
      >
        {PAGES.map((p) => (
          <Col
            key={p.kind}
            style={{
              minWidth: '100%',
              scrollSnapAlign: 'start',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '0 32px',
            }}
          >
            <OnboardingHero kind={p.kind} />
            <Spacer h={48} />
            <span style={{ color: colors.textPrimary, fontSize: 28, fontWeight: 700, textAlign: 'center' }}>
              {p.title}
            </span>
            <Spacer h={14} />
            <span style={{ color: colors.textSecondary, fontSize: 15, lineHeight: 1.45, textAlign: 'center' }}>
              {p.body}
            </span>
          </Col>
        ))}
      </div>

      {/* Bottom controls */}
      <Col
        style={{
          padding: '0 28px 28px',
          paddingBottom: 'calc(28px + var(--safe-bottom))',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'center', padding: '20px 0' }}>
          {PAGES.map((_, i) => {
            const active = i === page
            return (
              <div
                key={i}
                style={{
                  margin: '0 4px',
                  height: 7,
                  width: active ? 22 : 7,
                  borderRadius: 999,
                  background: active ? colors.accent : colors.outline,
                  transition: 'width 220ms ease, background 220ms ease',
                }}
              />
            )
          })}
        </div>
        <button
          onClick={next}
          style={{
            height: 56,
            borderRadius: 16,
            background: colors.accent,
            color: colors.onPrimary,
            fontSize: 16,
            fontWeight: 700,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: 8,
          }}
        >
          {isLast ? 'Get Started' : 'Next'}
          <IcArrowForward size={20} />
        </button>
      </Col>
    </div>
  )
}

const rad = (deg: number) => (deg * Math.PI) / 180

function OnboardingHero({ kind }: { kind: number }) {
  const ref = useRef<HTMLCanvasElement>(null)

  useEffect(() => {
    const canvas = ref.current!
    const ctx = canvas.getContext('2d')!
    const size = 240
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
    canvas.width = size * dpr
    canvas.height = size * dpr
    canvas.style.width = `${size}px`
    canvas.style.height = `${size}px`

    let raf = 0
    const loop = (now: number) => {
      const rot = ((now % 7000) / 7000) * 360
      const pulse = (now % 2400) / 2400
      draw(ctx, size, dpr, kind, rot, pulse)
      raf = requestAnimationFrame(loop)
    }
    raf = requestAnimationFrame(loop)
    return () => cancelAnimationFrame(raf)
  }, [kind])

  return <canvas ref={ref} />
}

function drawHeroPlane(ctx: CanvasRenderingContext2D, cx: number, cy: number, heading: number, color: string) {
  const s = 1.6
  ctx.save()
  ctx.translate(cx, cy)
  ctx.rotate(rad(heading))
  ctx.beginPath()
  ctx.moveTo(0, -11 * s)
  ctx.lineTo(3 * s, -2 * s)
  ctx.lineTo(12 * s, 4 * s)
  ctx.lineTo(12 * s, 6 * s)
  ctx.lineTo(3 * s, 3 * s)
  ctx.lineTo(3 * s, 9 * s)
  ctx.lineTo(6 * s, 12 * s)
  ctx.lineTo(0, 10 * s)
  ctx.lineTo(-6 * s, 12 * s)
  ctx.lineTo(-3 * s, 9 * s)
  ctx.lineTo(-3 * s, 3 * s)
  ctx.lineTo(-12 * s, 6 * s)
  ctx.lineTo(-12 * s, 4 * s)
  ctx.lineTo(-3 * s, -2 * s)
  ctx.closePath()
  ctx.fillStyle = color
  ctx.fill()
  ctx.restore()
}

function draw(ctx: CanvasRenderingContext2D, size: number, dpr: number, kind: number, rot: number, pulse: number) {
  ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
  ctx.clearRect(0, 0, size, size)
  const cx = size / 2
  const cy = size / 2
  const rMax = size / 2

  // glow
  const glow = ctx.createRadialGradient(cx, cy, 0, cx, cy, rMax)
  glow.addColorStop(0, withAlpha(colors.accent, 0.22))
  glow.addColorStop(1, 'transparent')
  ctx.fillStyle = glow
  ctx.beginPath()
  ctx.arc(cx, cy, rMax, 0, Math.PI * 2)
  ctx.fill()

  if (kind === 0) {
    for (let k = 1; k <= 3; k++) {
      ctx.beginPath()
      ctx.arc(cx, cy, (rMax * k) / 3.2, 0, Math.PI * 2)
      ctx.strokeStyle = withAlpha(colors.accent, 0.15)
      ctx.lineWidth = 2
      ctx.stroke()
    }
    ctx.beginPath()
    ctx.arc(cx, cy, rMax * pulse, 0, Math.PI * 2)
    ctx.strokeStyle = withAlpha(colors.accent, (1 - pulse) * 0.4)
    ctx.lineWidth = 2.5
    ctx.stroke()

    const r = rad(rot)
    ctx.beginPath()
    ctx.moveTo(cx, cy)
    ctx.lineTo(cx + Math.cos(r) * rMax * 0.9, cy + Math.sin(r) * rMax * 0.9)
    ctx.strokeStyle = withAlpha(colors.accent, 0.4)
    ctx.lineWidth = 2.5
    ctx.stroke()

    const planes: [number, number][] = [
      [40, 0.55],
      [160, 0.4],
      [280, 0.7],
    ]
    for (const [ang, dist] of planes) {
      const rr = rad(ang)
      const px = cx + Math.cos(rr) * rMax * dist
      const py = cy + Math.sin(rr) * rMax * dist
      drawHeroPlane(ctx, px, py, ang + 90, ang === 280 ? colors.amber : colors.planeIdle)
    }
  } else if (kind === 1) {
    const coneTopY = cy - rMax * 0.7
    const leftX = cx - rMax * 0.5
    const rightX = cx + rMax * 0.5
    const bottomY = cy + rMax * 0.6
    const grad = ctx.createLinearGradient(0, 0, 0, size)
    grad.addColorStop(0, withAlpha(colors.accent, 0.3))
    grad.addColorStop(1, 'transparent')
    ctx.beginPath()
    ctx.moveTo(leftX, bottomY)
    ctx.lineTo(cx, coneTopY)
    ctx.lineTo(rightX, bottomY)
    ctx.fillStyle = grad
    ctx.fill()
    ctx.beginPath()
    ctx.moveTo(leftX, bottomY)
    ctx.lineTo(cx, coneTopY)
    ctx.lineTo(rightX, bottomY)
    ctx.strokeStyle = withAlpha(colors.accent, 0.5)
    ctx.lineWidth = 2
    ctx.stroke()

    drawHeroPlane(ctx, cx, cy - rMax * 0.35, 90 + pulse * 4, colors.amber)

    ctx.beginPath()
    ctx.arc(cx, cy + rMax * 0.2, 4, 0, Math.PI * 2)
    ctx.fillStyle = colors.accent
    ctx.fill()
    ctx.beginPath()
    ctx.arc(cx, cy + rMax * 0.2, 12, 0, Math.PI * 2)
    ctx.strokeStyle = withAlpha(colors.accent, 0.4)
    ctx.lineWidth = 2
    ctx.stroke()
  } else {
    ctx.save()
    ctx.translate(cx, cy)
    ctx.rotate(rad(rot * 0.3))
    ctx.translate(-cx, -cy)
    const arcColors = [colors.accent, colors.amber, colors.planeIdle]
    for (let k = 0; k <= 2; k++) {
      const radius = rMax * (0.4 + k * 0.18)
      ctx.beginPath()
      ctx.arc(cx, cy, radius, rad(k * 100), rad(k * 100 + 80))
      ctx.strokeStyle = withAlpha(arcColors[k], 0.7)
      ctx.lineWidth = 4
      ctx.stroke()
    }
    ctx.restore()
    drawHeroPlane(ctx, cx, cy, 45, colors.accent)
  }
}
