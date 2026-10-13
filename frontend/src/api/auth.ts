import { AuthResponse } from '../types/auth'
import api from '../utils/api'

export const AuthService = {
	authenticate: () =>
		api.post<AuthResponse>('/auth/authenticate').then(res => res.data),
	refreshToken: (refreshToken: string) =>
		api
			.post<AuthResponse>('/auth/token/refresh', { refreshToken })
			.then(res => res.data),
	logout: () => api.post('/logout').then(res => res.data),
}
