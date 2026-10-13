import {
	InfiniteData,
	keepPreviousData,
	useInfiniteQuery,
	useMutation,
	useQueryClient,
} from '@tanstack/react-query'
import { AdminProductApi } from '../../api/admin/product'
import { ProductService } from '../../api/product'
import { ProductPageResponse } from '../../types/product'
/* ───────── поиск товаров (Elastic) ───────── */
export const useSearchProducts = (search: string) =>
	useInfiniteQuery<ProductPageResponse>({
		queryKey: ['admin-product-search', search],
		queryFn: ({ pageParam = 0 }) =>
			ProductService.search(search, pageParam as number, 20),
		enabled: !!search.trim(),
		initialPageParam: 0,
		getNextPageParam: last =>
			last.current_page < last.total_pages ? last.current_page + 1 : undefined,
		placeholderData: keepPreviousData,
	})

/* ───────── удаление ───────── */
export const useDeleteProduct = () => {
	const qc = useQueryClient()

	return useMutation<void, Error, number>({
		mutationFn: AdminProductApi.delete,
		onSuccess: (__, article) => {
			qc.setQueriesData<InfiniteData<ProductPageResponse>>(
				{ queryKey: ['admin-product-search'] },
				old => {
					if (!old) return old
					return {
						...old,
						pages: old.pages.map(p => ({
							...p,
							products: p.products.filter(pr => pr.article_number !== article),
						})),
					}
				}
			)
		},
	})
}
