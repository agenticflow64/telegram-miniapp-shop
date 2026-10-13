import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { AuthService } from '../api/auth'

export const useAuth = () => {
	const queryClient = useQueryClient()

	const {
		data: authData,
		isLoading: isAuthLoading,
		error: authError,
	} = useQuery({
		queryKey: ['auth'],
		queryFn: async () => {
			try {
				const data = await AuthService.authenticate()
				localStorage.setItem('accessToken', data.access_token)
				localStorage.setItem('refreshToken', data.refresh_token)
				return { isAuthenticated: true }
			} catch (error) {
				localStorage.removeItem('accessToken')
				localStorage.removeItem('refreshToken')
				throw error
			}
		},
		retry: 0,
		staleTime: Infinity,
	})

	const logout = useMutation({
		mutationFn: AuthService.logout,
		onSuccess: () => {
			localStorage.removeItem('accessToken')
			localStorage.removeItem('refreshToken')
			queryClient.clear()
		},
	})

	const refresh = useMutation({
		mutationFn: (refreshToken: string) =>
			AuthService.refreshToken(refreshToken),
		onSuccess: data => {
			localStorage.setItem('accessToken', data.access_token)
			localStorage.setItem('refreshToken', data.refresh_token)
		},
	})

	return {
		authData,
		isAuthLoading,
		authError,
		logout,
		refresh,
	}
}
