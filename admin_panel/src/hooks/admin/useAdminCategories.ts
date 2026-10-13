import { useMutation, useQueryClient } from '@tanstack/react-query'
import { AdminCategoryApi } from '../../api/admin/category'
import {
	AttributesAddRequestDTO,
	CategoryCreateRequestDTO,
	CategoryOperationResponseDTO,
} from '../../types/admin/category'

/* ── создать категорию или добавить новые атрибуты сразу ───────── */
export const useCreateOrUpdateCategory = () => {
	const qc = useQueryClient()

	return useMutation<
		CategoryOperationResponseDTO,
		Error,
		CategoryCreateRequestDTO
	>({
		mutationFn: AdminCategoryApi.createOrUpdate,
		onSuccess: () => {
			qc.invalidateQueries({ queryKey: ['categories'] })
		},
	})
}

/* ── добавить атрибуты к существующей категории ────────────────── */
export const useAddAttributes = () => {
	const qc = useQueryClient()

	return useMutation<
		CategoryOperationResponseDTO,
		Error,
		{ categoryId: number; body: AttributesAddRequestDTO }
	>({
		mutationFn: ({ categoryId, body }) =>
			AdminCategoryApi.addAttributes(categoryId, body),
		onSuccess: () => {
			qc.invalidateQueries({ queryKey: ['categories'] })
		},
	})
}
