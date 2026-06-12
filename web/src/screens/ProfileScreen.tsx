import { useState, type ReactNode } from 'react'
import { colors, withAlpha } from '../theme/colors'
import { Col, Row, Spacer } from '../components/ui'
import {
  IcBolt,
  IcChevronRight,
  IcDarkMode,
  IcInfo,
  IcMapStyle,
  IcNotifications,
  IcProfile,
  IcSpeed,
  IcStar,
} from '../components/icons'
import type { LucideIcon } from 'lucide-react'

export function ProfileScreen({ onOpenPaywall }: { onOpenPaywall: () => void }) {
  const [notifications, setNotifications] = useState(true)
  const [darkMode, setDarkMode] = useState(true)

  return (
    <Col
      className="no-scrollbar"
      style={{
        height: '100%',
        overflowY: 'auto',
        background: colors.bgBase,
        padding: '0 20px 24px',
        paddingTop: 'var(--safe-top)',
      }}
    >
      <Spacer h={12} />
      <span style={{ color: colors.textPrimary, fontSize: 26, fontWeight: 700 }}>Profile</span>
      <Spacer h={16} />

      {/* Account card */}
      <Row
        style={{
          borderRadius: 18,
          background: colors.surface1,
          border: `1px solid ${colors.outline}`,
          padding: 16,
        }}
      >
        <div
          style={{
            width: 54,
            height: 54,
            borderRadius: 999,
            background: colors.surface2,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <IcProfile size={28} color={colors.textSecondary} />
        </div>
        <Spacer w={14} />
        <Col style={{ flex: 1 }}>
          <span style={{ color: colors.textPrimary, fontSize: 17, fontWeight: 700 }}>Spotter</span>
          <span style={{ color: colors.textSecondary, fontSize: 13 }}>Free plan</span>
        </Col>
      </Row>

      <Spacer h={16} />

      {/* Pro banner */}
      <Row
        onClick={onOpenPaywall}
        style={{
          borderRadius: 18,
          background: `linear-gradient(135deg, ${withAlpha(colors.accent, 0.22)}, ${withAlpha(colors.amber, 0.18)})`,
          border: `1px solid ${withAlpha(colors.accent, 0.4)}`,
          padding: 16,
          cursor: 'pointer',
        }}
      >
        <div
          style={{
            width: 44,
            height: 44,
            borderRadius: 13,
            background: `linear-gradient(135deg, ${colors.accent}, ${colors.amber})`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <IcBolt size={22} color={colors.onPrimary} />
        </div>
        <Spacer w={14} />
        <Col style={{ flex: 1 }}>
          <span style={{ color: colors.textPrimary, fontSize: 16, fontWeight: 700 }}>Upgrade to Pro</span>
          <span style={{ color: colors.textSecondary, fontSize: 12 }}>AR, 3D, alerts & more</span>
        </Col>
        <IcChevronRight size={22} color={colors.textSecondary} />
      </Row>

      <Spacer h={22} />
      <GroupLabel text="Preferences" />
      <SettingsGroup>
        <ToggleRow icon={IcNotifications} label="Flight alerts" checked={notifications} onChange={setNotifications} />
        <Divider />
        <ToggleRow icon={IcDarkMode} label="Dark map" checked={darkMode} onChange={setDarkMode} />
        <Divider />
        <NavRow icon={IcMapStyle} label="Map style" value="Satellite" />
        <Divider />
        <NavRow icon={IcSpeed} label="Units" value="Knots · Feet" />
      </SettingsGroup>

      <Spacer h={18} />
      <GroupLabel text="About" />
      <SettingsGroup>
        <NavRow icon={IcStar} label="Rate the app" value={null} />
        <Divider />
        <NavRow icon={IcInfo} label="About Plane Finder" value="v1.0" />
      </SettingsGroup>

      <Spacer h={20} />
      <span style={{ color: colors.textMuted, fontSize: 12, textAlign: 'center' }}>UI/UX concept • mock data</span>
    </Col>
  )
}

function GroupLabel({ text }: { text: string }) {
  return (
    <span
      style={{
        color: colors.textMuted,
        fontSize: 11,
        fontWeight: 700,
        letterSpacing: 1,
        textTransform: 'uppercase',
        padding: '0 0 8px 4px',
      }}
    >
      {text}
    </span>
  )
}

function SettingsGroup({ children }: { children: ReactNode }) {
  return (
    <Col style={{ borderRadius: 18, background: colors.surface1, border: `1px solid ${colors.outline}` }}>
      {children}
    </Col>
  )
}

function Divider() {
  return <div style={{ height: 1, margin: '0 16px', background: colors.outline }} />
}

function ToggleRow({
  icon: Icon,
  label,
  checked,
  onChange,
}: {
  icon: LucideIcon
  label: string
  checked: boolean
  onChange: (v: boolean) => void
}) {
  return (
    <Row style={{ padding: '14px 16px' }}>
      <Icon size={22} color={colors.accent} />
      <Spacer w={14} />
      <span style={{ color: colors.textPrimary, fontSize: 15, flex: 1 }}>{label}</span>
      <Switch checked={checked} onChange={onChange} />
    </Row>
  )
}

function Switch({ checked, onChange }: { checked: boolean; onChange: (v: boolean) => void }) {
  return (
    <button
      onClick={() => onChange(!checked)}
      style={{
        width: 48,
        height: 28,
        borderRadius: 999,
        background: checked ? colors.accent : colors.surface2,
        border: `1px solid ${checked ? colors.accent : colors.outline}`,
        position: 'relative',
        transition: 'background 180ms ease',
        flex: '0 0 auto',
      }}
    >
      <span
        style={{
          position: 'absolute',
          top: 3,
          left: checked ? 23 : 3,
          width: 20,
          height: 20,
          borderRadius: 999,
          background: checked ? colors.onPrimary : colors.textSecondary,
          transition: 'left 180ms ease',
        }}
      />
    </button>
  )
}

function NavRow({ icon: Icon, label, value }: { icon: LucideIcon; label: string; value: string | null }) {
  return (
    <Row style={{ padding: '16px', cursor: 'pointer' }}>
      <Icon size={22} color={colors.accent} />
      <Spacer w={14} />
      <span style={{ color: colors.textPrimary, fontSize: 15, flex: 1 }}>{label}</span>
      {value != null && (
        <>
          <span style={{ color: colors.textSecondary, fontSize: 13 }}>{value}</span>
          <Spacer w={6} />
        </>
      )}
      <IcChevronRight size={20} color={colors.textMuted} />
    </Row>
  )
}
