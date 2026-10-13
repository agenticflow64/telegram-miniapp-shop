import { useQuery } from '@tanstack/react-query'
import { OrderService } from '../api/order'
import type { OrderPageResponse } from '../types/order'

export const useUserOrders = () => {
	return useQuery<OrderPageResponse>({
		queryKey: ['user-orders'],
		queryFn: () => OrderService.getOrders(0, 1000),
	})
}
