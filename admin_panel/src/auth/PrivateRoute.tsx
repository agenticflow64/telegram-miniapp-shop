import { Spin } from 'antd'
import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from './AuthContext'

export const PrivateRoute = () => {
	const { status } = useAuth()

	if (status === 'loading') return <Spin fullscreen />

	return status === 'authenticated' ? (
		<Outlet />
	) : (
		<Navigate to='/login' replace />
	)
}
