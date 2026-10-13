package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CategoryResponseDTO(
    @Schema(
        description = "Категории в формате: Название -> ID",
        example = "{\"Смартфоны\": 1, \"Ноутбуки\": 2}"
    )
    val categories: Map<String, Long>
)
