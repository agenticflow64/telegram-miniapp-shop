import react from '@vitejs/plugin-react-swc'
import { defineConfig } from 'vite'

// vite.config.ts (клиент Mini App)
export default defineConfig({
	plugins: [react()],
	server: {
		host: '0.0.0.0',
		port: 5173,
		strictPort: true,
		allowedHosts: ['localhost'],
		proxy: {
			'/api': {
				target: 'http://localhost:8080',
				changeOrigin: true,
			},
		},
	},
})
