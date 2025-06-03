import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/auth': {
        target: 'http://localhost:8091',
        changeOrigin: true,
        secure: false,
      },
      '/login': {
        target: 'http://localhost:8091',
        changeOrigin: true,
        secure: false,
      },

      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false,
      },
      
      '/register': {
        target: 'http://localhost:8091',
        changeOrigin: true,
        secure: false,
      }
    }
  },
});
