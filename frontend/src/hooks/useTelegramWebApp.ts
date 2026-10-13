// src/hooks/useTelegramWebApp.ts
import type { WebApp } from '@twa-dev/types'
import { useEffect, useState } from 'react'

export const useTelegramWebApp = () => {
	const [webApp, setWebApp] = useState<WebApp | null>(null)

	useEffect(() => {
		if (typeof window !== 'undefined' && window.Telegram?.WebApp) {
			setWebApp(window.Telegram.WebApp)
			window.Telegram.WebApp.ready()
		}
	}, [])

	return webApp
}
