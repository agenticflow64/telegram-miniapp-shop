export interface CreateOrderRequest {
	payment_method: 'CASH' | 'CARD' | 'CRYPTO'
	order_address: string
}
export type OrderStatus =
	| 'CREATED'
	| 'WAITING_FOR_PAYMENT'
	| 'PAID'
	| 'PROCESSING'
	| 'IN_PROGRESS'
	| 'IN_WAY'
	| 'DELIVERED'
	| 'CANCELLED'

export interface OrderDTO {
	id: number
	status: OrderStatus
	order_amount: number
	payment_method: string
	order_address: string
	expected_date: string
	created_at: string
	items: OrderItemDTO[]
}

export interface OrderItemDTO {
	article_number: number
	product_name: string
	quantity: number
	price: number
}

export interface OrderPageResponse {
	orders: OrderDTO[]
	current_page: number
	total_pages: number
	total_orders: number
}
