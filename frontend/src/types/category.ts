export interface CategoryResponse {
	categories: Record<string, number>
}

export interface CategoryAttributesResponse {
	category_id: number
	category_name: string
	attributes: AttributeDTO[]
}

export interface AttributeDTO {
	id: number
	name: string
	values: string[]
}
