package ru.webshop.backend.dto.admin

data class CategoryCreateRequestDTO(
    val categoryName: String,
    val attributes: List<String> = emptyList()
)