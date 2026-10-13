import { AddToCartRequest, CartItem, CartPageResponse } from '../types/cart'
import api from '../utils/api'

export const CartService = {
	addItem: (data: AddToCartRequest) =>
		api.post('/cart/add', data).then(res => res.data),
	removeItem: (article: number) =>
		api.delete(`/cart/remove?article=${article}`).then(res => res.data),
	getCart: (page: number = 0, size: number = 20) =>
		api
			.get<CartPageResponse>(`/cart/list?page=${page}&size=${size}`)
			.then(res => res.data),

	getAllCartItems: () =>
		api.get<CartPageResponse>('/cart/list?size=1000').then(res => ({
			...res.data,
			items: res.data.items.map(item => ({
				...item,
				key: `${item.article_number}-${item.quantity}`,
			})),
		})),

	checkInCart: (article: number) =>
		api
			.get<CartItem>(`/cart/checkin?article=${article}`)
			.then(res => res.data?.quantity || 0),
}
