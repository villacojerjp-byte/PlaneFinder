# Plane Finder — React + Capacitor (iOS App Store ready)

A faithful port of the native Android **Plane Finder** UI/UX clone (originally
Kotlin + Jetpack Compose, in [`../app`](../app)) to **React + TypeScript + Vite**,
wrapped with **Capacitor** so it ships as a native iOS app to the App Store.

All seven screens, the custom radar map, AR sky view, route arcs and onboarding
heroes are reproduced 1:1 using HTML5 `<canvas>` and CSS. Data is still mocked
(see [`src/data/mockData.ts`](src/data/mockData.ts)) — the focus is look, feel and
interaction.

| Android (Compose) | React port |
|---|---|
| `MainActivity.kt` | [`src/App.tsx`](src/App.tsx) — onboarding gate, bottom nav, AR/Paywall overlays |
| `screens/OnboardingScreen.kt` | [`src/screens/Onboarding.tsx`](src/screens/Onboarding.tsx) |
| `screens/MapScreen.kt` + `components/PlaneMap.kt` | [`src/screens/MapScreen.tsx`](src/screens/MapScreen.tsx) + [`src/components/PlaneMap.tsx`](src/components/PlaneMap.tsx) |
| `components/FlightDetail.kt` | [`src/components/FlightDetail.tsx`](src/components/FlightDetail.tsx) |
| `screens/ArScreen.kt` | [`src/screens/ArScreen.tsx`](src/screens/ArScreen.tsx) |
| `screens/BoardsScreen.kt` | [`src/screens/BoardsScreen.tsx`](src/screens/BoardsScreen.tsx) |
| `screens/PaywallScreen.kt` | [`src/screens/PaywallScreen.tsx`](src/screens/PaywallScreen.tsx) |
| `screens/ProfileScreen.kt` | [`src/screens/ProfileScreen.tsx`](src/screens/ProfileScreen.tsx) |
| `ui/theme/Color.kt` | [`src/theme/colors.ts`](src/theme/colors.ts) |
| `data/Models.kt`, `data/MockData.kt` | [`src/data/models.ts`](src/data/models.ts), [`src/data/mockData.ts`](src/data/mockData.ts) |

## Stack

- React 18 + TypeScript + Vite 5
- `lucide-react` for icons (mapped from Material icons in [`src/components/icons.tsx`](src/components/icons.tsx))
- Capacitor 6 (`@capacitor/ios`, `app`, `status-bar`, `splash-screen`)
- 100% custom canvas rendering for the map, AR view, route arcs and onboarding art

## Prerequisites

- **Node.js 18+** (built/verified here with Node 24 LTS)
- For the iOS build: a **Mac with Xcode 15+** and **CocoaPods** (`sudo gem install cocoapods`)

## Develop (any OS)

```bash
cd web
npm install
npm run dev          # http://localhost:5173 — open in a browser, use device toolbar
```

`npm run build` type-checks (`tsc --noEmit`) and bundles to `dist/`.

## Ship to the iOS App Store (on a Mac)

The web bundle and the native Xcode project in [`ios/`](ios) are already generated
and committed. From a Mac:

```bash
cd web
npm install
npm run build            # produce dist/
npx cap sync ios         # copy web assets + install CocoaPods deps
npx cap open ios         # open the project in Xcode
```

Then in **Xcode**:

1. Select the **App** target → **Signing & Capabilities** → pick your Team
   (Apple Developer account). Set a unique **Bundle Identifier** — the default is
   `com.planefinder.app` (change it in both Xcode and
   [`capacitor.config.ts`](capacitor.config.ts) → `appId`).
2. Set **Version** / **Build** numbers.
3. Replace the placeholder **App Icon** in
   `ios/App/App/Assets.xcassets/AppIcon.appiconset` with your 1024px icon set.
4. Pick a real device or **Any iOS Device**, then **Product → Archive**.
5. In the Organizer, **Distribute App → App Store Connect** to upload, then submit
   for review from [App Store Connect](https://appstoreconnect.apple.com).

> `npx cap add ios` was already run, so the `ios/` folder exists. After changing
> web code, just re-run `npm run build && npx cap copy ios` (or `npx cap sync ios`)
> before archiving. The `npm run ios` script does build + sync + open in one step.

## Going live with real flight data

The app reads from `src/data/mockData.ts`. To plug in a real feed, swap that module
for a small data layer that calls your backend (e.g. OpenSky for live positions +
AeroDataBox for airport boards, behind a thin proxy that holds your API keys), and
map the responses onto the existing `Flight` / `BoardEntry` types in
`src/data/models.ts` — no UI changes required.

## Notes

- iPhone orientation is locked to portrait (the design is a portrait phone UI).
- The status bar overlays the WebView; screens pad with `env(safe-area-inset-*)`.
- The `ios/` native project is committed; generated bits (`Pods/`, copied
  `public/`) are git-ignored and recreated by `cap sync`.
