/**
 *  CategoryService
 *  ---------------
 *  Используется в админ-панели только для чтения
 *  (select-списки в форме «добавить атрибуты»).
 *
 *  GET-эндпоинты публичные, поэтому достаточно обычного axios-инстанса
 *  (тот же, что настроен в `src/api/axios.ts` — он уже добавит /api prefix).
 */

import api from './axios'

/* ─── типы ─────────────────────────────────────────────────────────── */

export interface CategoryResponse {
	/** пара «имя → id», пример: { "Тетрадь": 5, "Ручка": 6 } */
	categories: Record<string, number>
}

export interface AttributeDTO {
	id: number
	name: string
	values: string[]
}

export interface CategoryAttributesResponse {
	category_id: number
	category_name: string
	attributes: AttributeDTO[]
}

/* ─── сервис ───────────────────────────────────────────────────────── */

export const CategoryService = {
	/** полный список категорий (имя → id) */
	getAll: () => api.get<CategoryResponse>('/categories').then(res => res.data),

	/** атрибуты конкретной категории */
	getAttributes: (categoryId: number) =>
		api
			.get<CategoryAttributesResponse>(`/categories/${categoryId}`)
			.then(res => res.data),
}
