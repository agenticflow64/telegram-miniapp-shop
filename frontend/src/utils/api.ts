import { retrieveRawInitData } from '@telegram-apps/sdk-react'
import axios from 'axios'
import { AuthService } from '../api/auth'

const api = axios.create({ baseURL: '/api' })

api.interceptors.request.use(config => {
	const initDataRaw = retrieveRawInitData()

	if (config.url === '/auth/authenticate') {
		if (initDataRaw) {
			config.headers['X-Telegram-Init-Data'] = initDataRaw
		}
		delete config.headers.Authorization
	} else {
		const accessToken = localStorage.getItem('accessToken')
		if (accessToken) {
			config.headers.Authorization = `Bearer ${accessToken}`
		}
		delete config.headers['X-Telegram-Init-Data']
	}

	return config
})

api.interceptors.response.use(
	response => response,
	async error => {
		const originalRequest = error.config
		if (error.response?.status === 403 && !originalRequest._retry) {
			originalRequest._retry = true

			try {
				const refreshToken = localStorage.getItem('refreshToken')
				const response = await AuthService.refreshToken(refreshToken!)

				localStorage.setItem('accessToken', response.access_token)
				localStorage.setItem('refreshToken', response.refresh_token)

				originalRequest.headers.Authorization = `Bearer ${response.access_token}`
				return api(originalRequest)
			} catch (refreshError) {
				localStorage.removeItem('accessToken')
				localStorage.removeItem('refreshToken')
				console.error('Refresh token failed:', refreshError)
				window.location.reload()
			}
		}
		return Promise.reject(error)
	}
)

export default api
