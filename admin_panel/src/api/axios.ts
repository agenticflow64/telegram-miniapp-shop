import axios from 'axios'

const api = axios.create({
	baseURL: '/api/admin',
})

api.interceptors.request.use(config => {
	const access = localStorage.getItem('accessToken')
	if (access && config.headers) {
		config.headers.Authorization = `Bearer ${access}`
	}
	return config
})

api.interceptors.response.use(
	resp => resp,
	async error => {
		const original = error.config
		if (error.response?.status === 403 && !original._retry) {
			original._retry = true

			const refresh = localStorage.getItem('refreshToken')
			if (!refresh) throw error

			try {
				const { data } = await api.post<{
					access_token: string
					refresh_token: string
				}>('/auth/refresh', { refreshToken: refresh })

				localStorage.setItem('accessToken', data.access_token)
				localStorage.setItem('refreshToken', data.refresh_token)
				original.headers.Authorization = `Bearer ${data.access_token}`
				return api(original)
			} catch (e) {
				localStorage.clear()
				window.location.href = '/'
				return Promise.reject(e)
			}
		}
		return Promise.reject(error)
	}
)

export default api
