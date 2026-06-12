import { statusMeta, type FlightStatus } from '../data/models'
import { withAlpha } from '../theme/colors'

export function StatusPill({ status }: { status: FlightStatus }) {
  const { label, color } = statusMeta[status]
  return (
    <div
      style={{
        display: 'inline-flex',
        flexDirection: 'row',
        alignItems: 'center',
        gap: 6,
        borderRadius: 999,
        background: withAlpha(color, 0.14),
        padding: '5px 10px',
      }}
    >
      <div style={{ width: 7, height: 7, borderRadius: 999, background: color }} />
      <span style={{ color, fontSize: 12, fontWeight: 600 }}>{label}</span>
    </div>
  )
}
