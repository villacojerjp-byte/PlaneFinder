import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// base: './' so built asset URLs are relative — required when the bundle is
// loaded from the native file:// scheme inside the Capacitor iOS WebView.
export default defineConfig({
  base: './',
  plugins: [react()],
  build: {
    outDir: 'dist',
    sourcemap: false,
  },
  server: {
    host: true,
    port: 5173,
  },
})
