/* ───────────────────────── Новый товар ────────────────────────── */

/**
 *  Тело запроса `POST /api/admin/products/create`
 *  👉 поля 1-в-1 с backend-DTO `ProductCreateRequestDTO`
 */
export interface ProductCreateRequestDTO {
	/** название товара */
	name: string
	/** цена (любая валюта в backend – у нас число) */
	price: number
	/** остаток на складе */
	balance_in_stock: number
	/** описание – опционально */
	description?: string
	/** ID категории (должна существовать) */
	category_id: number
	/** карта: ID-атрибута → выбранное значение */
	attributes: Record<string, string>
	/** сервисные поля (можно 0) */
	number_of_orders: number
	rating: number
}

/* ───────────────────────── Загрузка фото ───────────────────────── */

/**
 *  multipart-meta для `POST /api/admin/products/uploadPhoto`
 */
export interface PhotoUploadRequestDTO {
	/** артикул товара, который вернул предыдущий шаг */
	articleNumber: number
	/** порядковый номер фото (1-5) */
	indexNumber: number
}

export interface ProductAttributeDTO {
	attribute_id: number
	attribute_name: string
	value: string
}

export interface ProductResponse {
	id: number
	article_number: number
	name: string
	price: number
	rating: number
	number_of_orders: number
	category_id: number
	category_name: string
	balance_in_stock?: number
	description?: string
	attributes: ProductAttributeDTO[]
	photos: string[]
}

export interface ProductPageResponse {
	/** массив товаров текущей страницы */
	products: ProductResponse[]
	/** номер текущей страницы (0-based) */
	current_page: number
	/** всего страниц */
	total_pages: number
	/** всего товаров (опционально, возвращается не всегда) */
	total_products?: number
}

export interface PhotoElascticResponse {
	photo_id: number
	indexNumber: number
	created_at: string
	photo_url: string
	id: number
}
export interface ProductDocument {
	id: number
	name: string
	articleNumber: number
	balanceInStock?: number
	category?: string
	categoryId?: number
	description?: string
	numberOfOrders?: number
	price?: number
	rating?: number
	totalReviews?: number
	values: { id?: number; value?: string }[]
	photos?: PhotoElascticResponse[] | null
}

export interface ElasticPage<T> {
	content: T[]
	pageable: { pageNumber: number; pageSize: number }
	totalPages: number
	totalElements: number
	numberOfElements: number
	first: boolean
	last: boolean
}
