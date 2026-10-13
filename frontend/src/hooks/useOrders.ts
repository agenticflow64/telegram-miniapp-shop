import {
	keepPreviousData,
	useMutation,
	useQuery,
	useQueryClient,
} from '@tanstack/react-query'
import { OrderService } from '../api'
import { OrderDTO, OrderPageResponse } from '../types/order'

export const useOrders = (page: number) => {
	return useQuery<OrderPageResponse>({
		queryKey: ['orders', page],
		queryFn: () => OrderService.getOrders(page),
		placeholderData: keepPreviousData,
		select: data => ({
			...data,
			orders: data.orders.map(order => ({
				...order,
				key: order.id,
			})),
		}),
	})
}

export const useOrderActions = () => {
	const queryClient = useQueryClient()

	const createOrder = useMutation({
		mutationFn: OrderService.create,
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['orders', 'cart'] })
		},
	})

	const cancelOrder = useMutation<OrderDTO, Error, number>({
		mutationFn: orderId => OrderService.cancel(orderId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['orders'] })
		},
	})

	return { createOrder, cancelOrder }
}
