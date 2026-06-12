import { useEffect, useRef, useState } from 'react'
import { Capacitor } from '@capacitor/core'
import { App as CapApp } from '@capacitor/app'
import { StatusBar, Style } from '@capacitor/status-bar'
import { SplashScreen } from '@capacitor/splash-screen'
import { colors, withAlpha } from './theme/colors'
import { Col } from './components/ui'
import { Onboarding } from './screens/Onboarding'
import { MapScreen } from './screens/MapScreen'
import { BoardsScreen } from './screens/BoardsScreen'
import { ProfileScreen } from './screens/ProfileScreen'
import { ArScreen } from './screens/ArScreen'
import { PaywallScreen } from './screens/PaywallScreen'
import { IcAr, IcMap, IcProfile, IcSchedule } from './components/icons'
import type { LucideIcon } from 'lucide-react'

type Tab = 'MAP' | 'BOARDS' | 'PROFILE'

const TABS: { id: Tab | 'AR'; label: string; icon: LucideIcon }[] = [
  { id: 'MAP', label: 'Map', icon: IcMap },
  { id: 'BOARDS', label: 'Boards', icon: IcSchedule },
  { id: 'AR', label: 'AR', icon: IcAr },
  { id: 'PROFILE', label: 'Profile', icon: IcProfile },
]

/** Mount-on-demand slide-up overlay with enter + exit animation. */
function useSlideOverlay(open: boolean) {
  const [mounted, setMounted] = useState(open)
  const [shown, setShown] = useState(false)
  useEffect(() => {
    if (open) {
      setMounted(true)
      const id = requestAnimationFrame(() => setShown(true))
      return () => cancelAnimationFrame(id)
    }
    setShown(false)
    const t = window.setTimeout(() => setMounted(false), 300)
    return () => window.clearTimeout(t)
  }, [open])
  return { mounted, shown }
}

export default function App() {
  const [onboarded, setOnboarded] = useState(false)
  const [tab, setTab] = useState<Tab>('MAP')
  const [showAr, setShowAr] = useState(false)
  const [showPaywall, setShowPaywall] = useState(false)

  const ar = useSlideOverlay(showAr)
  const paywall = useSlideOverlay(showPaywall)

  // Latest UI state for the native back-button handler (avoids stale closures).
  const stateRef = useRef({ showAr, showPaywall, tab, onboarded })
  stateRef.current = { showAr, showPaywall, tab, onboarded }

  useEffect(() => {
    if (!Capacitor.isNativePlatform()) return
    StatusBar.setStyle({ style: Style.Dark }).catch(() => {})
    StatusBar.setOverlaysWebView({ overlay: true }).catch(() => {})
    SplashScreen.hide().catch(() => {})

    const handle = CapApp.addListener('backButton', () => {
      const s = stateRef.current
      if (s.showPaywall) setShowPaywall(false)
      else if (s.showAr) setShowAr(false)
      else if (s.onboarded && s.tab !== 'MAP') setTab('MAP')
      else CapApp.exitApp()
    })
    return () => {
      handle.then((h) => h.remove())
    }
  }, [])

  if (!onboarded) {
    return <Onboarding onFinish={() => setOnboarded(true)} />
  }

  return (
    <div style={{ position: 'relative', height: '100%', width: '100%', background: colors.bgBase, overflow: 'hidden' }}>
      {/* Main content + bottom bar */}
      <Col style={{ height: '100%' }}>
        <div style={{ flex: 1, position: 'relative', minHeight: 0 }}>
          {tab === 'MAP' && <MapScreen onOpenAr={() => setShowAr(true)} />}
          {tab === 'BOARDS' && <BoardsScreen />}
          {tab === 'PROFILE' && <ProfileScreen onOpenPaywall={() => setShowPaywall(true)} />}
        </div>
        <BottomBar
          current={tab}
          onSelect={(id) => {
            if (id === 'AR') setShowAr(true)
            else setTab(id)
          }}
        />
      </Col>

      {/* AR full-screen overlay */}
      {ar.mounted && (
        <SlideLayer shown={ar.shown}>
          <ArScreen onBack={() => setShowAr(false)} />
        </SlideLayer>
      )}

      {/* Paywall full-screen overlay */}
      {paywall.mounted && (
        <SlideLayer shown={paywall.shown}>
          <PaywallScreen onClose={() => setShowPaywall(false)} />
        </SlideLayer>
      )}
    </div>
  )
}

function SlideLayer({ shown, children }: { shown: boolean; children: React.ReactNode }) {
  return (
    <div
      style={{
        position: 'absolute',
        inset: 0,
        zIndex: 30,
        transform: shown ? 'translateY(0)' : 'translateY(100%)',
        transition: 'transform 300ms cubic-bezier(0.22, 1, 0.36, 1)',
      }}
    >
      {children}
    </div>
  )
}

function BottomBar({ current, onSelect }: { current: Tab; onSelect: (id: Tab | 'AR') => void }) {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-evenly',
        alignItems: 'center',
        background: colors.surface1,
        padding: '8px',
        paddingBottom: 'calc(8px + var(--safe-bottom))',
      }}
    >
      {TABS.map((t) => {
        if (t.id === 'AR') {
          return (
            <button key={t.id} onClick={() => onSelect('AR')} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <div
                style={{
                  width: 46,
                  height: 46,
                  borderRadius: 999,
                  background: colors.accent,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
              >
                <t.icon size={24} color={colors.onPrimary} />
              </div>
              <div style={{ height: 3 }} />
              <span style={{ color: colors.accent, fontSize: 11, fontWeight: 600 }}>{t.label}</span>
            </button>
          )
        }
        const selected = current === t.id
        const tint = selected ? colors.accent : colors.textMuted
        return (
          <button
            key={t.id}
            onClick={() => onSelect(t.id)}
            style={{
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              borderRadius: 14,
              padding: '6px 18px',
              background: selected ? withAlpha(colors.accent, 0.08) : 'transparent',
            }}
          >
            <t.icon size={24} color={tint} />
            <div style={{ height: 3 }} />
            <span style={{ color: tint, fontSize: 11, fontWeight: selected ? 600 : 400 }}>{t.label}</span>
          </button>
        )
      })}
    </div>
  )
}
