// Ported from app/src/main/java/com/planefinder/ui/ui/theme/Color.kt
// Deep "aviation night" palette.

export const colors = {
  // Backgrounds
  bgDeep: '#070B12',
  bgBase: '#0A1019',
  mapTop: '#0C1826',
  mapBottom: '#070D16',

  // Surfaces
  surface1: '#121A26',
  surface2: '#18222F',
  surface3: '#202C3B',
  outline: '#263241',

  // Map detail
  mapLand: '#132231',
  mapLandEdge: '#1C3346',
  mapGrid: 'rgba(62, 92, 120, 0.078)', // 0x143E5C78
  mapWater: '#0A1420',

  // Brand / accents
  accent: '#35C6F4', // sky cyan
  accentDim: '#1E7FA8',
  accentSoft: 'rgba(53, 198, 244, 0.2)',
  amber: '#FFB020',
  amberSoft: 'rgba(255, 176, 32, 0.2)',
  violet: '#8B7CF6',

  // Status
  green: '#34D399',
  red: '#F87171',
  yellow: '#FBBF24',

  // Text
  textPrimary: '#EAF1F8',
  textSecondary: '#93A1B3',
  textMuted: '#5E6C7D',

  // Plane markers
  planeIdle: '#D7E3F0',
  planeTrail: 'rgba(53, 198, 244, 0.333)',

  // On-accent foreground (dark text reads well on bright cyan/amber)
  onPrimary: '#04141C',
} as const

/** Convert a #RRGGBB hex to an rgba() string with the given alpha (0..1). */
export function withAlpha(hex: string, alpha: number): string {
  const h = hex.replace('#', '')
  const r = parseInt(h.slice(0, 2), 16)
  const g = parseInt(h.slice(2, 4), 16)
  const b = parseInt(h.slice(4, 6), 16)
  return `rgba(${r}, ${g}, ${b}, ${alpha})`
}
