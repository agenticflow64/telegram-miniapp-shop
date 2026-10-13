export interface AddToCartRequest {
	article_number: number
	quantity: number
}

export interface CartItem {
	id: number
	article_number: number
	product_name: string
	price: number
	quantity: number
	add_date: string
	key?: string
}

export interface CartPageResponse {
	items: CartItem[]
	current_page: number
	total_pages: number
	total_items: number
}
