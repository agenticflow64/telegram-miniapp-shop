export interface AttributeIdDTO {
	id: number
	name: string
	created: boolean
}

export interface CategoryOperationResponseDTO {
	categoryId: number
	categoryCreated: boolean
	attributes: AttributeIdDTO[]
}

/* ─── запросы ─── */
export interface CategoryCreateRequestDTO {
	categoryName: string
	attributes: string[]
}

export interface AttributesAddRequestDTO {
	attributes: string[]
}
