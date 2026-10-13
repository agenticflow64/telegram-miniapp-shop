import { QueryClient } from '@tanstack/react-query'

export const prefetchAllData = async (queryClient: QueryClient) => {
	await Promise.all([
		queryClient.prefetchQuery({ queryKey: ['products'] }),
		queryClient.prefetchQuery({ queryKey: ['categories'] }),
		queryClient.prefetchQuery({ queryKey: ['cart'] }),
	])
}
