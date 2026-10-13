import { useQuery } from '@tanstack/react-query'
import { CategoryService } from '../api/category'

export const useCategories = () => {
	return useQuery({
		queryKey: ['categories'],
		queryFn: CategoryService.getAll,
	})
}

export const useCategoryAttributes = (categoryId: number) => {
	return useQuery({
		queryKey: ['categoryAttributes', categoryId],
		queryFn: () => CategoryService.getAttributes(categoryId),
		enabled: !!categoryId,
	})
}
