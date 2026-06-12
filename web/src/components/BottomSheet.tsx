import { useEffect, useRef, useState, type ReactNode } from 'react'
import { colors } from '../theme/colors'

/**
 * Modal bottom sheet with scrim + slide-up animation. Mirrors Compose's
 * ModalBottomSheet. Manages its own exit animation before calling onClose.
 */
export function BottomSheet({ children, onClose }: { children: ReactNode; onClose: () => void }) {
  const [shown, setShown] = useState(false)
  // The tap that opens this sheet also emits a compatibility `click` at the
  // same point right after `pointerup`. Without this guard that ghost click
  // lands on the freshly-mounted backdrop and closes the sheet instantly.
  const openedAt = useRef(0)

  useEffect(() => {
    openedAt.current = performance.now()
    const id = requestAnimationFrame(() => setShown(true))
    return () => cancelAnimationFrame(id)
  }, [])

  const close = () => {
    if (performance.now() - openedAt.current < 350) return
    setShown(false)
    window.setTimeout(onClose, 280)
  }

  return (
    <div
      onClick={close}
      style={{
        position: 'fixed',
        inset: 0,
        zIndex: 40,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'flex-end',
        background: shown ? 'rgba(0,0,0,0.55)' : 'rgba(0,0,0,0)',
        transition: 'background 280ms ease',
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className="no-scrollbar"
        style={{
          background: colors.surface1,
          color: colors.textPrimary,
          borderTopLeftRadius: 24,
          borderTopRightRadius: 24,
          maxHeight: '88vh',
          overflowY: 'auto',
          transform: shown ? 'translateY(0)' : 'translateY(100%)',
          transition: 'transform 280ms cubic-bezier(0.22, 1, 0.36, 1)',
          paddingBottom: 'var(--safe-bottom)',
          boxShadow: '0 -8px 40px rgba(0,0,0,0.5)',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 12, paddingBottom: 4 }}>
          <div style={{ width: 40, height: 4, borderRadius: 2, background: colors.outline }} />
        </div>
        {children}
      </div>
    </div>
  )
}
