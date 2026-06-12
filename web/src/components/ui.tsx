import React from 'react'

type DivProps = React.HTMLAttributes<HTMLDivElement>

/** Flex column (mirrors Compose `Column`). */
export const Col = React.forwardRef<HTMLDivElement, DivProps>(({ style, ...rest }, ref) => (
  <div ref={ref} {...rest} style={{ display: 'flex', flexDirection: 'column', ...style }} />
))
Col.displayName = 'Col'

/** Flex row, vertically centered by default (the common Compose case). */
export const Row = React.forwardRef<HTMLDivElement, DivProps>(({ style, ...rest }, ref) => (
  <div
    ref={ref}
    {...rest}
    style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', ...style }}
  />
))
Row.displayName = 'Row'

/** Fixed gap, like Compose `Spacer(Modifier.width/height)`. */
export function Spacer({ w, h }: { w?: number; h?: number }) {
  return <div style={{ width: w, height: h, flex: '0 0 auto' }} />
}

/** Safe-area padding values usable in inline styles. */
export const safe = {
  top: 'var(--safe-top)',
  bottom: 'var(--safe-bottom)',
}
