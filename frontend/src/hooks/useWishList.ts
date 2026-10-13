import {
	keepPreviousData,
	useMutation,
	useQuery,
	useQueryClient,
} from '@tanstack/react-query'
import { WishListService } from '../api/wishlist'
import { WishListPageResponse } from '../types/wishlist'

export const useWishlist = (page: number = 1) => {
	return useQuery<WishListPageResponse, Error>({
		queryKey: ['wishlist', page],
		queryFn: () => WishListService.getList(page),
		placeholderData: keepPreviousData,
	})
}

export const useWishlistActions = () => {
	const queryClient = useQueryClient()

	const addToWishlist = useMutation({
		mutationFn: (article: number) => WishListService.addItem(article),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['wishlist'] })
			queryClient.invalidateQueries({ queryKey: ['wishlist-status'] })
			queryClient.invalidateQueries({ queryKey: ['products'] })
		},
	})

	const removeFromWishlist = useMutation({
		mutationFn: (article: number) => WishListService.removeItem(article),
		onSuccess: (_, article) => {
			queryClient.invalidateQueries({ queryKey: ['wishlist'] })
			queryClient.invalidateQueries({ queryKey: ['wishlist-status'] })
			queryClient.invalidateQueries({ queryKey: ['products'] })
			queryClient.setQueryData<WishListPageResponse>(['wishlist'], old => {
				if (!old) return old
				return {
					...old,
					items: old.items.filter(item => item.article_number !== article),
					totalItems: old.total_items - 1,
				}
			})
		},
	})

	return { addToWishlist, removeFromWishlist }
}
