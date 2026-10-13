import { WishListItem, WishListPageResponse } from '../types/wishlist'
import api from '../utils/api'

export const WishListService = {
	addItem: (article: number) =>
		api
			.post<WishListItem>(`/wishlist/add?article=${article}`)
			.then(res => res.data),

	removeItem: (article: number) =>
		api
			.delete<WishListItem>(`/wishlist/remove?article=${article}`)
			.then(res => res.data),

	getList: (page: number = 0, size: number = 20) =>
		api
			.get<WishListPageResponse>(`/wishlist/list?page=${page}&size=${size}`)
			.then(res => res.data),

	getAllWishlistItems: (): Promise<WishListPageResponse> =>
		api
			.get<WishListPageResponse>('/wishlist/list?size=1000')
			.then(res => res.data),

	checkInWishlist: (article: number) =>
		api
			.get<boolean>(`/wishlist/checkin?article=${article}`)
			.then(res => res.data),
}
