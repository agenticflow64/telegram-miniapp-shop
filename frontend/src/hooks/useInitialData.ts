import { useQueries } from '@tanstack/react-query'
import {
	CartService,
	OrderService,
	ProductService,
	WishListService,
} from '../api'
import { useAuth } from './useAuth'

export const useInitialData = (page: number = 0) => {
	const { authData } = useAuth()
	return useQueries({
		queries: [
			{
				queryKey: ['products', page],
				queryFn: () => ProductService.getAll(page),
				staleTime: 1000 * 60 * 5,
				enabled: !!authData?.isAuthenticated,
			},
			{
				queryKey: ['orders', page],
				queryFn: () => OrderService.getOrders(page),
				enabled: !!authData?.isAuthenticated,
			},
			{
				queryKey: ['wishlist', page],
				queryFn: () => WishListService.getList(page),
				enabled: !!authData?.isAuthenticated,
			},
			{
				queryKey: ['cart', page],
				queryFn: () => CartService.getCart(page),
				enabled: !!authData?.isAuthenticated,
			},
		],
	})
}
