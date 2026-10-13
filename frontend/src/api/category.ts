import { CategoryAttributesResponse, CategoryResponse } from '../types/category'
import api from '../utils/api'

export const CategoryService = {
	getAll: () => api.get<CategoryResponse>('/category').then(res => res.data),
	getAttributes: (categoryId: number) =>
		api
			.get<CategoryAttributesResponse>(`/category/${categoryId}`)
			.then(res => res.data),
}
