// hooks/useProductStatus.ts
import { useQuery } from '@tanstack/react-query'
import { CartService, WishListService } from '../api'

export const useProductStatus = () => {
	const cartQuery = useQuery({
		queryKey: ['cart-status'],
		queryFn: () => CartService.getAllCartItems(),
		staleTime: 1000 * 60 * 2,
		refetchOnWindowFocus: true,
	})

	const wishlistQuery = useQuery({
		queryKey: ['wishlist-status'],
		queryFn: () => WishListService.getAllWishlistItems(),
		staleTime: 1000 * 60 * 2,
		refetchOnWindowFocus: true,
	})

	const cartItems = cartQuery.data?.items || []
	const wishlistItems = wishlistQuery.data?.items || []

	return {
		cartMap: new Map(
			cartItems.map(item => [item.article_number, item.quantity])
		) as unknown as Map<number, number>,
		wishlistSet: new Set(
			wishlistItems.map(item => item.article_number)
		) as Set<number>,
		isLoading: cartQuery.isLoading || wishlistQuery.isLoading,
		error: cartQuery.error || wishlistQuery.error,
	}
}
