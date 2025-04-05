import tailwindcss from '@tailwindcss/vite';
import react from '@vitejs/plugin-react';
import path, { dirname } from 'path';
import { fileURLToPath } from 'url';
import { defineConfig } from 'vite';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      '@/components': path.resolve(__dirname, './src/shared/components'),
      '@': path.resolve(__dirname, './src'),
      '@components': path.resolve(__dirname, './src/shared/components'),
      '@lib': path.resolve(__dirname, './src/shared/lib'),
      '@ui': path.resolve(__dirname, './src/shared/components/ui'),
      '@hooks': path.resolve(__dirname, './src/shared/hooks'),

      '@student': path.resolve(__dirname, './src/features/student'),
      '@faculty': path.resolve(__dirname, './src/features/faculty'),
      '@program': path.resolve(__dirname, './src/features/program'),
      '@status': path.resolve(__dirname, './src/features/status'),
      '@settings': path.resolve(__dirname, './src/features/settings'),
      '@metadata': path.resolve(__dirname, './src/features/metadata'),

      '@config': path.resolve(__dirname, './src/core/config'),
      '@utils': path.resolve(__dirname, './src/shared/utils'),
      '@data': path.resolve(__dirname, './src/shared/data'),
      '@types': path.resolve(__dirname, './src/core/types'),
      '@assets': path.resolve(__dirname, './src/assets'),
    },
  },
});
