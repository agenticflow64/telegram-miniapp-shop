// src/hooks/useTelegramAuth.ts
import { retrieveRawInitData } from '@telegram-apps/sdk-react'
import { useEffect, useState } from 'react'

interface TelegramUser {
	id: number
	first_name: string
	last_name?: string
	username?: string
	photo_url?: string
	auth_date?: number
	hash?: string
}

export const useTelegramUser = (): TelegramUser | null => {
	const [user, setUser] = useState<TelegramUser | null>(null)

	useEffect(() => {
		const initDataRaw = retrieveRawInitData()
		if (!initDataRaw) return

		try {
			const urlParams = new URLSearchParams(initDataRaw)
			const userStr = urlParams.get('user')
			if (!userStr) return

			const userData = JSON.parse(decodeURIComponent(userStr))
			setUser({
				id: userData.id,
				first_name: userData.first_name,
				last_name: userData.last_name,
				username: userData.username,
				photo_url: userData.photo_url,
			})
		} catch (error) {
			console.error('Failed to parse Telegram user data:', error)
		}
	}, [])

	return user
}
