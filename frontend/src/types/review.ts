export interface ReviewDTO {
	id?: number
	rating: number
	text: string
	created_at?: string
	photos?: string[]
	telegram_id?: number
}

export interface ReviewResponse {
	id: number
	rating: number
	text: string
}

export interface PhotoReviewRequest {
	review_id: number
	index_number: number
}
