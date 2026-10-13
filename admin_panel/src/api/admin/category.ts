import {
	AttributesAddRequestDTO,
	CategoryCreateRequestDTO,
	CategoryOperationResponseDTO,
} from '../../types/admin/category'
import api from '../axios'

export const AdminCategoryApi = {
	createOrUpdate: (body: CategoryCreateRequestDTO) =>
		api
			.post<CategoryOperationResponseDTO>('/categories/create-or-update', body)
			.then(r => r.data),

	addAttributes: (categoryId: number, body: AttributesAddRequestDTO) =>
		api
			.post<CategoryOperationResponseDTO>(
				`/categories/${categoryId}/attributes`,
				body
			)
			.then(r => r.data),

	/* вспомогательно: «только категория» */
	createOnly: (name: string) =>
		api
			.post<CategoryOperationResponseDTO>('/categories/only-category', {
				categoryName: name,
			})
			.then(r => r.data),
}
