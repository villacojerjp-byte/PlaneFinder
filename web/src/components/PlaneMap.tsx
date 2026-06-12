import { useEffect, useRef } from 'react'
import type { Flight } from '../data/models'
import { colors, withAlpha } from '../theme/colors'

interface PlanePos {
  x: number
  y: number
  heading: number
  speed: number
}

interface Props {
  flights: Flight[]
  selectedId: string | null
  onSelect: (id: string | null) => void
}

const FONT = '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, system-ui, sans-serif'
const rad = (deg: number) => (deg * Math.PI) / 180

function roundRect(ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, r: number) {
  ctx.beginPath()
  ctx.moveTo(x + r, y)
  ctx.arcTo(x + w, y, x + w, y + h, r)
  ctx.arcTo(x + w, y + h, x, y + h, r)
  ctx.arcTo(x, y + h, x, y, r)
  ctx.arcTo(x, y, x + w, y, r)
  ctx.closePath()
}

export function PlaneMap({ flights, selectedId, onSelect }: Props) {
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const wrapRef = useRef<HTMLDivElement>(null)

  // Mutable, render-loop-owned state (kept in refs to avoid React churn).
  const positions = useRef<PlanePos[]>([])
  const scale = useRef(1)
  const offset = useRef({ x: 0, y: 0 })
  const sizeRef = useRef({ w: 0, h: 0 })
  const selectedRef = useRef(selectedId)
  const onSelectRef = useRef(onSelect)
  selectedRef.current = selectedId
  onSelectRef.current = onSelect

  // Re-seed positions whenever the flight list identity changes.
  useEffect(() => {
    positions.current = flights.map((f) => ({
      x: f.x,
      y: f.y,
      heading: f.headingDeg,
      speed: f.speedFactor,
    }))
  }, [flights])

  const screenOf = (nx: number, ny: number) => {
    const { w, h } = sizeRef.current
    const cx = w / 2
    const cy = h / 2
    const bx = nx * w
    const by = ny * h
    return {
      x: cx + (bx - cx) * scale.current + offset.current.x,
      y: cy + (by - cy) * scale.current + offset.current.y,
    }
  }

  const clampOffset = () => {
    const { w, h } = sizeRef.current
    const maxX = (w * (scale.current - 1)) / 2 + w * 0.15
    const maxY = (h * (scale.current - 1)) / 2 + h * 0.15
    offset.current.x = Math.max(-maxX, Math.min(maxX, offset.current.x))
    offset.current.y = Math.max(-maxY, Math.min(maxY, offset.current.y))
  }

  // ---- Canvas sizing (DPR-aware) ----
  useEffect(() => {
    const canvas = canvasRef.current!
    const wrap = wrapRef.current!
    const ro = new ResizeObserver(() => {
      const r = wrap.getBoundingClientRect()
      sizeRef.current = { w: r.width, h: r.height }
      const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
      canvas.width = Math.round(r.width * dpr)
      canvas.height = Math.round(r.height * dpr)
      canvas.style.width = `${r.width}px`
      canvas.style.height = `${r.height}px`
    })
    ro.observe(wrap)
    return () => ro.disconnect()
  }, [])

  // ---- Pointer gestures: 1 finger pan, 2 finger pinch, tap to select ----
  useEffect(() => {
    const wrap = wrapRef.current!
    const pts = new Map<number, { x: number; y: number }>()
    let prevDist = 0
    let prevMid = { x: 0, y: 0 }
    let downX = 0
    let downY = 0
    let maxMove = 0
    let multi = false

    const dist = (a: { x: number; y: number }, b: { x: number; y: number }) =>
      Math.hypot(a.x - b.x, a.y - b.y)
    const mid = (a: { x: number; y: number }, b: { x: number; y: number }) => ({
      x: (a.x + b.x) / 2,
      y: (a.y + b.y) / 2,
    })

    const local = (e: PointerEvent) => {
      const r = wrap.getBoundingClientRect()
      return { x: e.clientX - r.left, y: e.clientY - r.top }
    }

    const onDown = (e: PointerEvent) => {
      wrap.setPointerCapture(e.pointerId)
      const p = local(e)
      pts.set(e.pointerId, p)
      if (pts.size === 1) {
        downX = p.x
        downY = p.y
        maxMove = 0
        multi = false
      } else if (pts.size === 2) {
        multi = true
        const [a, b] = [...pts.values()]
        prevDist = dist(a, b)
        prevMid = mid(a, b)
      }
    }

    const onMove = (e: PointerEvent) => {
      if (!pts.has(e.pointerId)) return
      const prev = pts.get(e.pointerId)!
      const p = local(e)
      pts.set(e.pointerId, p)

      if (pts.size === 1) {
        offset.current.x += p.x - prev.x
        offset.current.y += p.y - prev.y
        maxMove = Math.max(maxMove, Math.hypot(p.x - downX, p.y - downY))
        clampOffset()
      } else if (pts.size === 2) {
        const [a, b] = [...pts.values()]
        const d = dist(a, b)
        const m = mid(a, b)
        if (prevDist > 0) {
          scale.current = Math.max(1, Math.min(4, scale.current * (d / prevDist)))
        }
        offset.current.x += m.x - prevMid.x
        offset.current.y += m.y - prevMid.y
        prevDist = d
        prevMid = m
        clampOffset()
      }
    }

    const onUp = (e: PointerEvent) => {
      const wasSingle = pts.size === 1 && !multi
      pts.delete(e.pointerId)
      if (wrap.hasPointerCapture(e.pointerId)) wrap.releasePointerCapture(e.pointerId)
      if (wasSingle && maxMove < 10) {
        // Tap → nearest plane within 70px wins, else clear selection.
        let hit: string | null = null
        let best = Number.MAX_VALUE
        flights.forEach((f, i) => {
          const pos = positions.current[i]
          if (!pos) return
          const s = screenOf(pos.x, pos.y)
          const dd = Math.hypot(s.x - downX, s.y - downY)
          if (dd < 70 && dd < best) {
            best = dd
            hit = f.id
          }
        })
        onSelectRef.current(hit)
      }
    }

    const onWheel = (e: WheelEvent) => {
      e.preventDefault()
      scale.current = Math.max(1, Math.min(4, scale.current * (1 - e.deltaY * 0.0015)))
      clampOffset()
    }

    wrap.addEventListener('pointerdown', onDown)
    wrap.addEventListener('pointermove', onMove)
    wrap.addEventListener('pointerup', onUp)
    wrap.addEventListener('pointercancel', onUp)
    wrap.addEventListener('wheel', onWheel, { passive: false })
    return () => {
      wrap.removeEventListener('pointerdown', onDown)
      wrap.removeEventListener('pointermove', onMove)
      wrap.removeEventListener('pointerup', onUp)
      wrap.removeEventListener('pointercancel', onUp)
      wrap.removeEventListener('wheel', onWheel)
    }
  }, [flights])

  // ---- Render + simulation loop ----
  useEffect(() => {
    const canvas = canvasRef.current!
    const ctx = canvas.getContext('2d')!
    let raf = 0
    let last = 0

    const loop = (now: number) => {
      const dt = last === 0 ? 0 : (now - last) / 1000
      last = now
      // advance plane positions
      if (dt > 0) {
        for (const p of positions.current) {
          const r = rad(p.heading)
          const v = 0.012 * p.speed * dt
          p.x += Math.sin(r) * v
          p.y += -Math.cos(r) * v
          if (p.x < -0.05) p.x = 1.05
          if (p.x > 1.05) p.x = -0.05
          if (p.y < -0.05) p.y = 1.05
          if (p.y > 1.05) p.y = -0.05
        }
      }
      const pulse = ((now % 2600) / 2600)
      const sweep = ((now % 6000) / 6000) * 360
      draw(ctx, pulse, sweep)
      raf = requestAnimationFrame(loop)
    }
    raf = requestAnimationFrame(loop)
    return () => cancelAnimationFrame(raf)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [flights])

  const draw = (ctx: CanvasRenderingContext2D, pulse: number, sweep: number) => {
    const { w, h } = sizeRef.current
    if (w === 0 || h === 0) return
    const dpr = Math.min(window.devicePixelRatio || 1, 2.5)
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0)

    // Background gradient (untransformed)
    const bg = ctx.createLinearGradient(0, 0, 0, h)
    bg.addColorStop(0, colors.mapTop)
    bg.addColorStop(1, colors.mapBottom)
    ctx.fillStyle = bg
    ctx.fillRect(0, 0, w, h)

    // Map content (pan + zoom about center)
    ctx.save()
    ctx.translate(offset.current.x, offset.current.y)
    ctx.translate(w / 2, h / 2)
    ctx.scale(scale.current, scale.current)
    ctx.translate(-w / 2, -h / 2)

    drawLandmasses(ctx, w, h)
    drawGraticule(ctx, w, h)
    drawHomeRings(ctx, w, h, pulse, sweep)
    flights.forEach((f, i) => {
      const p = positions.current[i]
      if (p) drawTrail(ctx, p.x * w, p.y * h, f.headingDeg)
    })
    flights.forEach((f, i) => {
      const p = positions.current[i]
      if (p) drawPlane(ctx, p.x * w, p.y * h, f.headingDeg, f.id === selectedRef.current, pulse)
    })
    ctx.restore()

    // Selected callout (screen space)
    const sel = flights.findIndex((f) => f.id === selectedRef.current)
    if (sel >= 0 && positions.current[sel]) {
      const f = flights[sel]
      const s = screenOf(positions.current[sel].x, positions.current[sel].y)
      drawCallout(ctx, s.x + 16, s.y - 14, f)
    }
  }

  return (
    <div
      ref={wrapRef}
      style={{
        position: 'absolute',
        inset: 0,
        background: colors.mapBottom,
        touchAction: 'none',
        overflow: 'hidden',
      }}
    >
      <canvas ref={canvasRef} />
    </div>
  )
}

// ---- Canvas drawing helpers (ported from PlaneMap.kt) ----

function drawGraticule(ctx: CanvasRenderingContext2D, w: number, h: number) {
  ctx.strokeStyle = colors.mapGrid
  ctx.lineWidth = 1
  const step = w / 8
  for (let x = 0; x <= w + 0.5; x += step) {
    ctx.beginPath()
    ctx.moveTo(x, 0)
    ctx.lineTo(x, h)
    ctx.stroke()
  }
  const stepY = h / 12
  for (let y = 0; y <= h + 0.5; y += stepY) {
    ctx.beginPath()
    ctx.moveTo(0, y)
    ctx.lineTo(w, y)
    ctx.stroke()
  }
}

function drawLandmasses(ctx: CanvasRenderingContext2D, w: number, h: number) {
  const blobs: [number, number][][] = [
    [[-0.05, 0.10], [0.18, 0.05], [0.30, 0.16], [0.26, 0.30], [0.34, 0.40], [0.22, 0.52], [0.10, 0.48], [-0.05, 0.55]],
    [[0.55, 0.08], [0.78, 0.04], [0.95, 0.14], [1.05, 0.30], [0.88, 0.34], [0.74, 0.26], [0.60, 0.30], [0.52, 0.18]],
    [[0.62, 0.62], [0.82, 0.58], [0.92, 0.72], [0.80, 0.92], [0.64, 0.96], [0.56, 0.80]],
    [[0.04, 0.74], [0.20, 0.70], [0.30, 0.82], [0.22, 0.98], [0.06, 1.02]],
  ]
  for (const pts of blobs) {
    ctx.beginPath()
    pts.forEach(([px, py], i) => {
      if (i === 0) ctx.moveTo(px * w, py * h)
      else ctx.lineTo(px * w, py * h)
    })
    ctx.closePath()
    ctx.fillStyle = colors.mapLand
    ctx.fill()
    ctx.strokeStyle = colors.mapLandEdge
    ctx.lineWidth = 1.2
    ctx.stroke()
  }
}

function drawHomeRings(ctx: CanvasRenderingContext2D, w: number, h: number, pulse: number, sweep: number) {
  const cx = w * 0.48
  const cy = h * 0.74
  const maxR = Math.min(w, h) * 0.30
  for (let k = 1; k <= 3; k++) {
    ctx.beginPath()
    ctx.arc(cx, cy, (maxR * k) / 3, 0, Math.PI * 2)
    ctx.strokeStyle = withAlpha(colors.accent, 0.1)
    ctx.lineWidth = 1
    ctx.stroke()
  }
  ctx.beginPath()
  ctx.arc(cx, cy, maxR * pulse, 0, Math.PI * 2)
  ctx.strokeStyle = withAlpha(colors.accent, (1 - pulse) * 0.35)
  ctx.lineWidth = 2
  ctx.stroke()

  const r = rad(sweep)
  ctx.beginPath()
  ctx.moveTo(cx, cy)
  ctx.lineTo(cx + Math.cos(r) * maxR, cy + Math.sin(r) * maxR)
  ctx.strokeStyle = withAlpha(colors.accent, 0.25)
  ctx.lineWidth = 2
  ctx.stroke()

  ctx.beginPath()
  ctx.arc(cx, cy, 5, 0, Math.PI * 2)
  ctx.fillStyle = colors.accent
  ctx.fill()
  ctx.beginPath()
  ctx.arc(cx, cy, 11, 0, Math.PI * 2)
  ctx.fillStyle = withAlpha(colors.accent, 0.25)
  ctx.fill()
}

function drawTrail(ctx: CanvasRenderingContext2D, cx: number, cy: number, heading: number) {
  const r = rad(heading)
  const dx = Math.sin(r)
  const dy = -Math.cos(r)
  const len = 46
  ctx.save()
  ctx.setLineDash([3, 5])
  ctx.strokeStyle = colors.planeTrail
  ctx.lineWidth = 2.2
  ctx.beginPath()
  ctx.moveTo(cx - dx * len, cy - dy * len)
  ctx.lineTo(cx, cy)
  ctx.stroke()
  ctx.restore()
}

function drawPlane(
  ctx: CanvasRenderingContext2D,
  cx: number,
  cy: number,
  heading: number,
  selected: boolean,
  pulse: number,
) {
  const color = selected ? colors.amber : colors.planeIdle
  const s = selected ? 1.25 : 1
  if (selected) {
    ctx.beginPath()
    ctx.arc(cx, cy, 10 + pulse * 22, 0, Math.PI * 2)
    ctx.fillStyle = withAlpha(colors.amber, (1 - pulse) * 0.5)
    ctx.fill()
    ctx.beginPath()
    ctx.arc(cx, cy, 16, 0, Math.PI * 2)
    ctx.fillStyle = withAlpha(colors.amber, 0.18)
    ctx.fill()
  }
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
  ctx.lineTo(6 * s, 13.5 * s)
  ctx.lineTo(0, 11 * s)
  ctx.lineTo(-6 * s, 13.5 * s)
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

function drawCallout(ctx: CanvasRenderingContext2D, x: number, y: number, f: Flight) {
  const padX = 10
  const padY = 6
  const gap = 8
  const fl = `FL${Math.round(f.altitudeFt / 100)}`
  const spd = `${f.groundSpeedKt}kt`
  ctx.textBaseline = 'middle'
  ctx.font = `bold 12px ${FONT}`
  const wNum = ctx.measureText(f.flightNumber).width
  ctx.font = `11px ${FONT}`
  const wFl = ctx.measureText(fl).width
  const wSpd = ctx.measureText(spd).width
  const innerW = wNum + gap + wFl + gap + wSpd
  const boxW = innerW + padX * 2
  const boxH = 14 + padY * 2

  roundRect(ctx, x, y, boxW, boxH, 8)
  ctx.fillStyle = 'rgba(26, 34, 48, 0.95)'
  ctx.fill()

  const midY = y + boxH / 2
  let tx = x + padX
  ctx.font = `bold 12px ${FONT}`
  ctx.fillStyle = colors.amber
  ctx.fillText(f.flightNumber, tx, midY)
  tx += wNum + gap
  ctx.font = `11px ${FONT}`
  ctx.fillStyle = colors.planeIdle
  ctx.fillText(fl, tx, midY)
  tx += wFl + gap
  ctx.fillStyle = colors.textSecondary
  ctx.fillText(spd, tx, midY)
}
