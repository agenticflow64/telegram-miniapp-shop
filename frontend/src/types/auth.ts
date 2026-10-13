export interface AuthResponse {
	access_token: string
	access_expires_at: string
	issued_at: string
	refresh_token: string
	refresh_expires_at: string
}
export interface TelegramUser {
	id: number
	first_name: string
	last_name?: string
	username?: string
	photo_url?: string
	auth_date?: number
	hash?: string
}
