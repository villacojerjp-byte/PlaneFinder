import { useState } from 'react'
import { arrivals, departures, homeAirport } from '../data/mockData'
import type { BoardEntry } from '../data/models'
import { colors, withAlpha } from '../theme/colors'
import { Col, Row, Spacer } from '../components/ui'
import { AirlineBadge } from '../components/AirlineBadge'
import { StatusPill } from '../components/StatusPill'
import { IcExpandMore, IcFlightLand, IcFlightTakeoff } from '../components/icons'
import type { LucideIcon } from 'lucide-react'

export function BoardsScreen() {
  const [departuresView, setDeparturesView] = useState(true)
  const entries = departuresView ? departures : arrivals

  return (
    <Col style={{ height: '100%', background: colors.bgBase, paddingTop: 'var(--safe-top)' }}>
      {/* Header */}
      <Row style={{ padding: '16px 20px' }}>
        <div
          style={{
            width: 46,
            height: 46,
            borderRadius: 13,
            background: withAlpha(colors.accent, 0.16),
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <span style={{ color: colors.accent, fontSize: 14, fontWeight: 900 }}>{homeAirport.code}</span>
        </div>
        <Spacer w={12} />
        <Col style={{ flex: 1 }}>
          <Row>
            <span style={{ color: colors.textPrimary, fontSize: 18, fontWeight: 700 }}>{homeAirport.name}</span>
            <IcExpandMore size={20} color={colors.textSecondary} />
          </Row>
          <span style={{ color: colors.textSecondary, fontSize: 12 }}>Flight information display</span>
        </Col>
      </Row>

      {/* Segmented toggle */}
      <Row
        style={{
          margin: '0 20px',
          borderRadius: 14,
          background: colors.surface1,
          border: `1px solid ${colors.outline}`,
          padding: 4,
          gap: 4,
        }}
      >
        <SegTab label="Departures" icon={IcFlightTakeoff} selected={departuresView} onClick={() => setDeparturesView(true)} />
        <SegTab label="Arrivals" icon={IcFlightLand} selected={!departuresView} onClick={() => setDeparturesView(false)} />
      </Row>

      <Spacer h={14} />

      {/* Column header */}
      <Row style={{ padding: '6px 24px' }}>
        <span style={{ color: colors.textMuted, fontSize: 10, fontWeight: 700, letterSpacing: 1, width: 56 }}>TIME</span>
        <span style={{ color: colors.textMuted, fontSize: 10, fontWeight: 700, letterSpacing: 1, flex: 1 }}>FLIGHT</span>
        <span style={{ color: colors.textMuted, fontSize: 10, fontWeight: 700, letterSpacing: 1 }}>
          {departuresView ? 'TO' : 'FROM'}
        </span>
      </Row>

      {/* List */}
      <Col className="no-scrollbar" style={{ flex: 1, overflowY: 'auto', padding: '6px 16px 16px', gap: 8 }}>
        {entries.map((e, i) => (
          <BoardRow key={i} e={e} />
        ))}
      </Col>
    </Col>
  )
}

function SegTab({
  label,
  icon: Icon,
  selected,
  onClick,
}: {
  label: string
  icon: LucideIcon
  selected: boolean
  onClick: () => void
}) {
  return (
    <button
      onClick={onClick}
      style={{
        flex: 1,
        borderRadius: 11,
        background: selected ? colors.accent : 'transparent',
        padding: '11px 0',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 7,
        transition: 'background 200ms ease',
      }}
    >
      <Icon size={18} color={selected ? colors.onPrimary : colors.textSecondary} />
      <span style={{ color: selected ? colors.onPrimary : colors.textSecondary, fontSize: 14, fontWeight: 700 }}>
        {label}
      </span>
    </button>
  )
}

function BoardRow({ e }: { e: BoardEntry }) {
  return (
    <Row
      style={{
        borderRadius: 14,
        background: colors.surface1,
        border: `1px solid ${colors.outline}`,
        padding: '12px 14px',
      }}
    >
      <span
        style={{
          color: colors.textPrimary,
          fontSize: 16,
          fontWeight: 700,
          fontFamily: 'var(--font-mono)',
          width: 56,
        }}
      >
        {e.time}
      </span>
      <Spacer w={8} />
      <AirlineBadge code={e.airlineCode} size={36} />
      <Spacer w={12} />
      <Col style={{ flex: 1, minWidth: 0 }}>
        <span style={{ color: colors.textPrimary, fontSize: 15, fontWeight: 600 }}>
          {e.cityCode} · {e.city}
        </span>
        <Row style={{ gap: 10 }}>
          <span style={{ color: colors.textSecondary, fontSize: 12, fontFamily: 'var(--font-mono)' }}>
            {e.flightNumber}
          </span>
          <span style={{ color: colors.textMuted, fontSize: 12 }}>Gate {e.gate}</span>
          <span style={{ color: colors.textMuted, fontSize: 12 }}>T{e.terminal}</span>
        </Row>
      </Col>
      <Spacer w={8} />
      <StatusPill status={e.status} />
    </Row>
  )
}
