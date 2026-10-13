import api from './axios'

export interface AuthResponseDTO {
	access_token: string
	refresh_token: string
	access_expires_at: string
	refresh_expires_at: string
}

export const AuthService = {
	login: (login: string, password: string) =>
		api
			.post<AuthResponseDTO>('/auth/login', { login, password })
			.then(r => r.data),
}
