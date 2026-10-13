import { useMutation } from '@tanstack/react-query'
import { AuthService } from '../api/auth'

export const useRefreshToken = () => {
	return useMutation({
		mutationFn: (refreshToken: string) =>
			AuthService.refreshToken(refreshToken),
		onSuccess: data => {
			localStorage.setItem('accessToken', data.access_token)
			localStorage.setItem('refreshToken', data.refresh_token)
		},
	})
}
