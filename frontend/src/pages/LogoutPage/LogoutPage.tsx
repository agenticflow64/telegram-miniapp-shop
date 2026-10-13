// src/pages/LogoutPage/LogoutPage.tsx
import { Spin, Typography } from 'antd'
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import { useTelegramWebApp } from '../../hooks/useTelegramWebApp'
import styles from './LogoutPage.module.scss'

const { Text } = Typography

export const LogoutPage = () => {
	const navigate = useNavigate()
	const { logout } = useAuth()
	const webApp = useTelegramWebApp()

	useEffect(() => {
		const performLogout = async () => {
			try {
				await logout.mutateAsync()

				if (webApp) {
					webApp.close()
				}
			} catch (error) {
				console.error('Logout failed:', error)
				navigate('/', { replace: true })
			}
		}

		performLogout()
	}, [logout, navigate, webApp])

	return (
		<div className={styles.container}>
			<Spin size='large' />
			<Text className={styles.text}>Завершаем сеанс...</Text>
		</div>
	)
}
