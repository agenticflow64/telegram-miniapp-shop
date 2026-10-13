import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { CartService } from '../api/cart'
import { CartPageResponse } from '../types/cart'

export const useCart = (page: number = 0) => {
	return useQuery({
		queryKey: ['cart', page],
		queryFn: () => CartService.getCart(page),
		placeholderData: previousData => previousData,
	})
}

export const useAllCartItems = () => {
	return useQuery({
		queryKey: ['cart', 'all'],
		queryFn: () => CartService.getAllCartItems(),
		staleTime: 1000 * 60 * 2,
	})
}

export const useCartCheckout = () => {
	const queryClient = useQueryClient()

	const getCartSnapshot = () => {
		const cartData = queryClient.getQueryData<CartPageResponse>(['cart'])
		const allItems =
			queryClient.getQueryData<CartPageResponse>(['cart', 'all'])?.items || []

		return {
			...cartData,
			items: allItems.length > 0 ? allItems : cartData?.items || [],
		}
	}

	return { getCartSnapshot }
}
export const useCartActions = () => {
	const queryClient = useQueryClient()

	const addToCart = useMutation({
		mutationFn: CartService.addItem,
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['cart'] })
			queryClient.invalidateQueries({ queryKey: ['cart-status'] })
			queryClient.invalidateQueries({ queryKey: ['products'] })
		},
	})

	const removeFromCart = useMutation({
		mutationFn: CartService.removeItem,
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['cart'] })
			queryClient.invalidateQueries({ queryKey: ['cart-status'] })
			queryClient.invalidateQueries({ queryKey: ['products'] })
		},
	})

	return { addToCart, removeFromCart }
}
