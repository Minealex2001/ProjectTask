import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  base: '/ProjectTaskWeb/', // Cambia esto si el repo tiene otro nombre
});
