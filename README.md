# Plane Finder — UI/UX Clone (Jetpack Compose)

A polished, **UI/UX-only** recreation of a *Plane Finder* style flight-tracker app,
built as a native Android app with **Kotlin + Jetpack Compose**. All data is mocked —
there is no real network/flight feed — the focus is the look, feel, and interactions.

## What's inside

| Screen | Highlights |
|--------|-----------|
| **Onboarding** | 3-page swipeable intro with animated radar / AR / data heroes |
| **Live Map** | Custom-drawn dark radar map, ~12 animated aircraft, pinch-zoom & pan, tap a plane to select, radar sweep + range rings, live count pill, search bar & filter chips |
| **Flight Detail** | Bottom sheet with route arc + progress, live telemetry (altitude, speed, heading, vertical rate), aircraft info, AR / follow / share actions |
| **AR Sky View** | Simulated camera sky with horizon, compass strip, reticle, and aircraft overlays placed by bearing/elevation; tap to identify |
| **Boards** | Departures / Arrivals flight information display with airline badges, gates, status pills |
| **Pro Paywall** | Feature list, monthly/yearly plans, free-trial CTA |
| **Profile** | Account, upgrade banner, preference toggles & settings |

Dark "aviation night" theme throughout, custom Canvas drawing for the map, AR overlay,
route arcs and onboarding illustrations, plus smooth animations.

## Requirements

- **Android Studio** (Ladybug / 2024.2 or newer recommended)
- Android SDK Platform **34** + Build-Tools 34 (already detected on this machine)
- JDK 17+ (Android Studio's bundled JBR works)
- Min SDK 24, Target/Compile SDK 34

## Run it

1. Open **Android Studio** → **Open** → select the `PlaneFinderUI` folder.
2. Let Gradle sync finish (first sync downloads dependencies).
3. Pick an emulator (or a USB device) and press **Run ▶**.

### Or from the command line
```bash
# Windows
gradlew.bat assembleDebug      # build the debug APK
gradlew.bat installDebug       # install on a running emulator/device

# macOS / Linux
./gradlew assembleDebug
```
The APK lands in `app/build/outputs/apk/debug/app-debug.apk`.

## Tech

- Kotlin 2.0 + Jetpack Compose (Material 3, BOM 2024.08)
- 100% Compose UI, no XML layouts
- Custom `Canvas` rendering for the map, AR view and illustrations
- Mock data in `app/src/main/java/com/planefinder/ui/data/MockData.kt`

## Project layout
```
app/src/main/java/com/planefinder/ui/
├── MainActivity.kt          # root, bottom nav, onboarding gate, overlays
├── data/                    # Flight models + mock data
├── ui/theme/                # colors, type, theme
├── components/              # PlaneMap, FlightDetail, shared widgets
└── screens/                 # Onboarding, Map, AR, Boards, Paywall, Profile
```

> Concept/demo app for UI/UX exploration. Not affiliated with Plane Finder / Pinkfroot.
