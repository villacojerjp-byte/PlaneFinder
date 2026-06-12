import { useEffect, useRef } from 'react'

/**
 * requestAnimationFrame loop. The callback receives the delta since the last
 * frame (seconds) and total elapsed time (seconds). Mirrors Compose's
 * `withFrameNanos` / `rememberInfiniteTransition` driving.
 */
export function useRafLoop(callback: (dt: number, elapsed: number) => void) {
  const cbRef = useRef(callback)
  cbRef.current = callback

  useEffect(() => {
    let raf = 0
    let last = 0
    let start = 0
    const loop = (t: number) => {
      if (start === 0) start = t
      const dt = last === 0 ? 0 : (t - last) / 1000
      last = t
      cbRef.current(dt, (t - start) / 1000)
      raf = requestAnimationFrame(loop)
    }
    raf = requestAnimationFrame(loop)
    return () => cancelAnimationFrame(raf)
  }, [])
}
