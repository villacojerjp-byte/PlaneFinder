import { useState } from 'react'
import { colors, withAlpha } from '../theme/colors'
import { Col, Row, Spacer } from '../components/ui'
import { IcBolt, IcCheck, IcClose, IcFilter, IcLayers, IcMap, IcNotifications, IcAr } from '../components/icons'
import type { LucideIcon } from 'lucide-react'

interface Perk {
  icon: LucideIcon
  title: string
  sub: string
}

export function PaywallScreen({ onClose }: { onClose: () => void }) {
  const [yearly, setYearly] = useState(true)
  const perks: Perk[] = [
    { icon: IcAr, title: 'Augmented reality', sub: 'Identify any aircraft overhead' },
    { icon: IcMap, title: '3D & cockpit view', sub: 'Fly along in immersive 3D' },
    { icon: IcFilter, title: 'Unlimited filters', sub: 'Airline, altitude, type & more' },
    { icon: IcLayers, title: 'Weather & airspace', sub: 'Live overlays on the map' },
    { icon: IcNotifications, title: 'Flight alerts', sub: 'Status, gate & delay notifications' },
    { icon: IcBolt, title: 'No ads, ever', sub: 'A clean, focused experience' },
  ]

  return (
    <div
      style={{
        position: 'absolute',
        inset: 0,
        background: `linear-gradient(180deg, ${colors.bgDeep}, ${colors.bgBase})`,
      }}
    >
      <Col
        className="no-scrollbar"
        style={{
          height: '100%',
          overflowY: 'auto',
          padding: '0 22px 160px',
          paddingTop: 'var(--safe-top)',
        }}
      >
        {/* Close */}
        <Row style={{ justifyContent: 'flex-end', paddingTop: 8 }}>
          <button
            onClick={onClose}
            style={{
              width: 36,
              height: 36,
              borderRadius: 999,
              background: colors.surface1,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <IcClose size={20} color={colors.textSecondary} />
          </button>
        </Row>

        <Spacer h={8} />

        {/* Hero badge */}
        <div
          style={{
            width: 86,
            height: 86,
            borderRadius: 24,
            alignSelf: 'center',
            background: `linear-gradient(135deg, ${colors.accent}, ${colors.amber})`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <IcBolt size={44} color={colors.onPrimary} />
        </div>

        <Spacer h={20} />
        <span style={{ color: colors.textPrimary, fontSize: 28, fontWeight: 700, textAlign: 'center' }}>
          Plane Finder Pro
        </span>
        <Spacer h={8} />
        <span style={{ color: colors.textSecondary, fontSize: 14, lineHeight: 1.4, textAlign: 'center' }}>
          Unlock the full sky. Everything you need to track, identify and follow flights.
        </span>

        <Spacer h={24} />

        {perks.map((perk) => (
          <Row key={perk.title} style={{ padding: '8px 0' }}>
            <div
              style={{
                width: 40,
                height: 40,
                borderRadius: 12,
                background: colors.surface2,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <perk.icon size={20} color={colors.accent} />
            </div>
            <Spacer w={14} />
            <Col style={{ flex: 1 }}>
              <span style={{ color: colors.textPrimary, fontSize: 15, fontWeight: 600 }}>{perk.title}</span>
              <span style={{ color: colors.textSecondary, fontSize: 12 }}>{perk.sub}</span>
            </Col>
            <IcCheck size={20} color={colors.green} />
          </Row>
        ))}

        <Spacer h={20} />

        {/* Plans */}
        <Row style={{ gap: 12, alignItems: 'stretch' }}>
          <PlanCard
            title="Yearly"
            price="$39.99"
            per="per year"
            note="Just $3.33/mo"
            badge="SAVE 40%"
            selected={yearly}
            onClick={() => setYearly(true)}
          />
          <PlanCard
            title="Monthly"
            price="$5.99"
            per="per month"
            note="Billed monthly"
            badge={null}
            selected={!yearly}
            onClick={() => setYearly(false)}
          />
        </Row>
      </Col>

      {/* Sticky CTA */}
      <Col
        style={{
          position: 'absolute',
          left: 0,
          right: 0,
          bottom: 0,
          padding: '24px 22px 18px',
          paddingBottom: 'calc(18px + var(--safe-bottom))',
          background: `linear-gradient(180deg, transparent, ${colors.bgBase} 40%, ${colors.bgBase})`,
        }}
      >
        <button
          onClick={onClose}
          style={{
            height: 56,
            borderRadius: 16,
            background: colors.accent,
            color: colors.onPrimary,
            fontSize: 16,
            fontWeight: 700,
          }}
        >
          Start 7-day free trial
        </button>
        <Spacer h={10} />
        <span style={{ color: colors.textMuted, fontSize: 12, textAlign: 'center' }}>
          Then {yearly ? '$39.99/year' : '$5.99/month'} • Cancel anytime  •  Restore
        </span>
      </Col>
    </div>
  )
}

function PlanCard({
  title,
  price,
  per,
  note,
  badge,
  selected,
  onClick,
}: {
  title: string
  price: string
  per: string
  note: string
  badge: string | null
  selected: boolean
  onClick: () => void
}) {
  return (
    <Col
      onClick={onClick}
      style={{
        flex: 1,
        borderRadius: 18,
        background: selected ? withAlpha(colors.accent, 0.12) : colors.surface1,
        border: `${selected ? 2 : 1}px solid ${selected ? colors.accent : colors.outline}`,
        padding: 16,
        cursor: 'pointer',
      }}
    >
      <Row style={{ justifyContent: 'space-between' }}>
        <span style={{ color: colors.textPrimary, fontSize: 15, fontWeight: 700 }}>{title}</span>
        {badge && (
          <span
            style={{
              borderRadius: 999,
              background: colors.amber,
              color: colors.onPrimary,
              fontSize: 9,
              fontWeight: 900,
              padding: '3px 8px',
            }}
          >
            {badge}
          </span>
        )}
      </Row>
      <Spacer h={12} />
      <span style={{ color: colors.textPrimary, fontSize: 24, fontWeight: 700 }}>{price}</span>
      <span style={{ color: colors.textSecondary, fontSize: 12 }}>{per}</span>
      <Spacer h={6} />
      <span style={{ color: selected ? colors.accent : colors.textMuted, fontSize: 12, fontWeight: 600 }}>{note}</span>
    </Col>
  )
}
