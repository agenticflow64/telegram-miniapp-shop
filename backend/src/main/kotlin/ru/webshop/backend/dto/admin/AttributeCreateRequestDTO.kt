package ru.webshop.backend.dto.admin

data class AttributeCreateRequestDTO(
    val name: String,
    val values: List<String>
)