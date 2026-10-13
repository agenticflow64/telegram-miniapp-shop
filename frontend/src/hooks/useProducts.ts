import {
	keepPreviousData,
	useInfiniteQuery,
	useQuery,
} from '@tanstack/react-query'
import { useSearchParams } from 'react-router-dom'
import { ProductService } from '../api/product'
import type { ProductPageResponse, ProductResponse } from '../types/product'
import { useProductStatus } from './useProductStatus'

export const useProducts = () => {
	const [searchParams] = useSearchParams()
	//const page = Number(searchParams.get('page')) || 0
	const categoryId = Number(searchParams.get('categoryId')) || undefined
	const sort = searchParams.get('sort') || undefined
	const searchQuery = searchParams.get('search') || undefined

	const { cartMap, wishlistSet, isLoading: statusLoading } = useProductStatus()

	const query = useInfiniteQuery<ProductPageResponse>({
		queryKey: [
			'products',
			{
				categoryId,
				sort,
				search: searchQuery,
				...Object.fromEntries(searchParams),
			},
		],
		queryFn: ({ pageParam = 0 }) => {
			const filters = Array.from(searchParams.entries()).reduce(
				(acc, [key, value]) => {
					if (key.startsWith('attributes[')) {
						const attrId = key.match(/\[(.*?)\]/)?.[1]
						if (attrId) {
							acc[Number(attrId)] = [...(acc[Number(attrId)] || []), value]
						}
					}
					return acc
				},
				{} as Record<number, string[]>
			)

			if (searchQuery) {
				return ProductService.search(searchQuery, pageParam as number)
			}

			if (categoryId) {
				return ProductService.searchWithFilters(
					categoryId,
					filters,
					pageParam as number,
					20,
					sort
				)
			}

			return ProductService.getAll(pageParam as number, 20, sort)
		},
		initialPageParam: 0,
		getNextPageParam: lastPage => {
			return lastPage.current_page < lastPage.total_pages
				? lastPage.current_page + 1
				: undefined
		},
		placeholderData: keepPreviousData,
		staleTime: 1000 * 60 * 5,
		enabled: !statusLoading,
	})

	const enrichedProducts =
		query.data?.pages.flatMap(page =>
			page.products.map(product => ({
				...product,
				inCart: cartMap.get(product.article_number) || 0,
				inWishlist: wishlistSet.has(product.article_number),
			}))
		) || []

	return {
		...query,
		data: {
			products: enrichedProducts,
			current_page: query.data?.pages.at(-1)?.current_page || 0,
			total_pages: query.data?.pages.at(-1)?.total_pages || 0,
		},
	}
}
export const useProductDetails = (article: number) => {
	return useQuery<ProductResponse, Error>({
		queryKey: ['product', article],
		queryFn: () => ProductService.getProduct(article),
		enabled: !!article,
	})
}
// *
