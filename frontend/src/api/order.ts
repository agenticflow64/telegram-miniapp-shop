import { CreateOrderRequest, OrderDTO, OrderPageResponse } from '../types/order'
import api from '../utils/api'

export const OrderService = {
	create: (data: CreateOrderRequest) =>
		api.post<OrderDTO>('/orders/create', data).then(res => res.data),
	cancel: (orderId: number) =>
		api
			.delete<OrderDTO>(`/orders/cancel?orderId=${orderId}`)
			.then(res => res.data),
	getOrders: (page: number, size: number = 20) =>
		api
			.get<OrderPageResponse>(`/orders/list?page=${page}&size=${size}`)
			.then(res => res.data),
}
