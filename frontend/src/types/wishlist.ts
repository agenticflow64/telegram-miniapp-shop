export interface WishListItem {
	id: number
	article_number: number
	product_name: string
	price: number
	add_date: string
}

export interface WishListPageResponse {
	items: WishListItem[]
	current_page: number
	total_pages: number
	total_items: number
}
