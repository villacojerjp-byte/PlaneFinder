import { withAlpha } from '../theme/colors'

/** Brand-ish color per airline code so badges feel distinct. */
export function airlineColor(code: string): string {
  switch (code) {
    case 'UA': return '#2E5BBA'
    case 'AA': return '#C8102E'
    case 'DL': return '#9B1B30'
    case 'LH': return '#0A2240'
    case 'EK': return '#D71921'
    case 'SQ': return '#1A3A6B'
    case 'QF': return '#E0001B'
    case 'AF': return '#002157'
    case 'NH': return '#13448F'
    case 'BA': return '#1D3C6E'
    case 'KE': return '#1A4FA0'
    case 'WN': return '#F9B612'
    case 'AS': return '#01426A'
    case 'B6': return '#003876'
    case 'VS': return '#E10A0A'
    case 'AC': return '#D22630'
    case 'JL': return '#CE0E2D'
    default: return '#2E5BBA'
  }
}

export function AirlineBadge({ code, size = 40 }: { code: string; size?: number }) {
  const bg = airlineColor(code)
  return (
    <div
      style={{
        width: size,
        height: size,
        flex: '0 0 auto',
        borderRadius: size * 0.28,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: `linear-gradient(135deg, ${withAlpha(bg, 0.95)}, ${withAlpha(bg, 0.65)})`,
      }}
    >
      <span
        style={{
          color: '#fff',
          fontWeight: 900,
          fontSize: size * 0.38,
          fontFamily: 'var(--font-sans)',
          letterSpacing: 0.2,
        }}
      >
        {code}
      </span>
    </div>
  )
}
