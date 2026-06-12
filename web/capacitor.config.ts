import type { CapacitorConfig } from '@capacitor/cli'

const config: CapacitorConfig = {
  appId: 'com.planefinder.app',
  appName: 'Plane Finder',
  webDir: 'dist',
  backgroundColor: '#0A1019',
  ios: {
    backgroundColor: '#0A1019',
    contentInset: 'never',
  },
  plugins: {
    SplashScreen: {
      launchShowDuration: 600,
      backgroundColor: '#0A1019',
      showSpinner: false,
    },
  },
}

export default config
