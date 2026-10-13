import { ReviewDTO } from './review'

export interface ProductResponse {
	id: number
	article_number: number
	name: string
	price: number
	rating: number
	number_of_orders: number
	attributes: ProductAttributeDTO[]
	category_id: number
	category_name: string
	photos: string[]
	reviews: ReviewDTO[]
	inCart?: number
	inWishlist?: boolean
}

export interface ProductAttributeDTO {
	attribute_id: number
	attribute_name: string
	value: string
}

export interface ProductPageResponse {
	products: ProductResponse[]
	current_page: number
	total_pages: number
	total_products: number
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
