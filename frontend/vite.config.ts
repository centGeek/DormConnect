import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/api/auth': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        secure: false,
      },
      '/api/event': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        secure: false,
      },
      '/api/chat': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        secure: false,
      },
      '/api/dorm-problem': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        secure: false,
      },
    }
  },
});
