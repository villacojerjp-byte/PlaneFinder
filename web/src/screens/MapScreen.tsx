import { useState } from 'react'
import { flights } from '../data/mockData'
import { colors, withAlpha } from '../theme/colors'
import { Col, Row, Spacer } from '../components/ui'
import { PlaneMap } from '../components/PlaneMap'
import { FlightDetail } from '../components/FlightDetail'
import { BottomSheet } from '../components/BottomSheet'
import { IcAr, IcLayers, IcMapStyle, IcMyLocation, IcSearch, IcTune } from '../components/icons'
import type { LucideIcon } from 'lucide-react'

const FILTERS = ['All', 'En Route', 'Arrivals', 'Departures', 'Emergency']

export function MapScreen({ onOpenAr }: { onOpenAr: () => void }) {
  const [selectedId, setSelectedId] = useState<string | null>(null)
  const [activeFilter, setActiveFilter] = useState('All')

  const selected = flights.find((f) => f.id === selectedId) ?? null

  return (
    <div style={{ position: 'absolute', inset: 0, overflow: 'hidden' }}>
      <PlaneMap flights={flights} selectedId={selectedId} onSelect={setSelectedId} />

      {/* Top controls */}
      <Col
        style={{
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          padding: '10px 16px',
          paddingTop: 'calc(10px + var(--safe-top))',
        }}
      >
        <SearchBar />
        <Spacer h={12} />
        <div className="no-scrollbar" style={{ display: 'flex', gap: 8, overflowX: 'auto' }}>
          {FILTERS.map((f) => (
            <FilterChip key={f} label={f} selected={f === activeFilter} onClick={() => setActiveFilter(f)} />
          ))}
        </div>
      </Col>

      {/* Right-side floating tools */}
      <Col style={{ position: 'absolute', right: 14, top: '50%', transform: 'translateY(-50%)', gap: 12 }}>
        <MapTool icon={IcLayers} />
        <MapTool icon={IcMapStyle} />
        <MapTool icon={IcMyLocation} />
        <MapTool icon={IcAr} highlight onClick={onOpenAr} />
      </Col>

      {/* Bottom live status pill */}
      <div style={{ position: 'absolute', bottom: 18, left: 0, right: 0, display: 'flex', justifyContent: 'center' }}>
        <LiveStatus count={flights.length} />
      </div>

      {selected && (
        <BottomSheet onClose={() => setSelectedId(null)}>
          <FlightDetail flight={selected} onArView={onOpenAr} />
        </BottomSheet>
      )}
    </div>
  )
}

function SearchBar() {
  return (
    <Row
      style={{
        borderRadius: 16,
        background: withAlpha(colors.surface1, 0.92),
        border: `1px solid ${colors.outline}`,
        padding: '14px',
        backdropFilter: 'blur(8px)',
      }}
    >
      <IcSearch size={20} color={colors.textSecondary} />
      <Spacer w={10} />
      <span style={{ color: colors.textMuted, fontSize: 14, flex: 1 }}>Search flights, airports, routes…</span>
      <div
        style={{
          width: 30,
          height: 30,
          borderRadius: 9,
          background: withAlpha(colors.accent, 0.16),
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <IcTune size={18} color={colors.accent} />
      </div>
    </Row>
  )
}

function FilterChip({ label, selected, onClick }: { label: string; selected: boolean; onClick: () => void }) {
  return (
    <button
      onClick={onClick}
      style={{
        flex: '0 0 auto',
        borderRadius: 999,
        background: selected ? colors.accent : withAlpha(colors.surface1, 0.9),
        border: `1px solid ${selected ? 'transparent' : colors.outline}`,
        color: selected ? colors.onPrimary : colors.textSecondary,
        fontSize: 13,
        fontWeight: 600,
        padding: '9px 16px',
      }}
    >
      {label}
    </button>
  )
}

function MapTool({ icon: Icon, highlight = false, onClick }: { icon: LucideIcon; highlight?: boolean; onClick?: () => void }) {
  return (
    <button
      onClick={onClick}
      style={{
        width: 48,
        height: 48,
        borderRadius: 999,
        background: highlight ? colors.accent : withAlpha(colors.surface1, 0.92),
        border: `1px solid ${highlight ? 'transparent' : colors.outline}`,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        backdropFilter: 'blur(8px)',
      }}
    >
      <Icon size={22} color={highlight ? colors.onPrimary : colors.textPrimary} />
    </button>
  )
}

function LiveStatus({ count }: { count: number }) {
  return (
    <Row
      style={{
        borderRadius: 999,
        background: withAlpha(colors.surface2, 0.95),
        border: `1px solid ${colors.outline}`,
        padding: '10px 16px',
        gap: 8,
        backdropFilter: 'blur(8px)',
      }}
    >
      <div style={{ width: 8, height: 8, borderRadius: 999, background: colors.green }} />
      <span style={{ color: colors.textPrimary, fontSize: 13, fontWeight: 600 }}>{count} aircraft tracked</span>
      <span style={{ color: colors.green, fontSize: 13, fontWeight: 600 }}>• live</span>
    </Row>
  )
}
