import { ReactNode, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthService } from '../api/auth'
import { AuthContext, AuthStatus } from './AuthContext'

export const AuthProvider = ({ children }: { children: ReactNode }) => {
	const navigate = useNavigate()

	const [status, setStatus] = useState<AuthStatus>('loading')
	const [token, setToken] = useState<string | undefined>(undefined)

	/* ───── первичная проверка токена ───── */
	useEffect(() => {
		const stored = localStorage.getItem('accessToken')
		if (stored) {
			setStatus('authenticated')
			setToken(stored)
		} else {
			setStatus('unauthenticated')
		}
	}, [])

	/* ───── методы ───── */
	const login = async (login: string, password: string) => {
		const data = await AuthService.login(login, password)
		localStorage.setItem('accessToken', data.access_token)
		localStorage.setItem('refreshToken', data.refresh_token)
		setStatus('authenticated')
		setToken(data.access_token)
		navigate('/')
	}

	const logout = () => {
		localStorage.clear()
		setStatus('unauthenticated')
		setToken(undefined)
		navigate('/login')
	}

	const value = { status, token, login, logout }

	return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
