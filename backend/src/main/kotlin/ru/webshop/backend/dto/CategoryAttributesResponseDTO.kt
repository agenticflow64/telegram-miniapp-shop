package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CategoryAttributesResponseDTO(
    @Schema(description = "ID категории", example = "5")
    val categoryId: Long,

    @Schema(description = "Название категории", example = "Смартфоны")
    val categoryName: String,

    @Schema(description = "Список атрибутов категории")
    val attributes: List<AttributeDTO>
)