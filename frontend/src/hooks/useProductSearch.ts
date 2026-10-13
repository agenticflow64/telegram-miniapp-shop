// import { useQuery } from '@tanstack/react-query'
// import { ProductService } from '../api/product'

// export const useProductSearch = (
// 	categoryId: number,
// 	filters: Record<number, string[]>,
// 	page: number = 1
// ) => {
// 	return useQuery({
// 		queryKey: ['products', 'search', categoryId, filters, page],
// 		queryFn: () => ProductService.search(categoryId, filters, page),
// 		staleTime: 1000 * 60 * 5,
// 	})
// }
