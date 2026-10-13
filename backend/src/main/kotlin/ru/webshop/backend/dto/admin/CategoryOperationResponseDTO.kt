package ru.webshop.backend.dto.admin

data class CategoryOperationResponseDTO(
    val categoryId: Long,
    val categoryCreated: Boolean,
    val attributes: List<AttributeIdDTO>
)