package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductPageResponseDTO(
    @Schema(description = "Список товаров")
    val products: List<ProductResponseDTO>,

    @Schema(
        description = "Текущая страница",
        example = "1",
        minimum = "0"
    )
    val currentPage: Int,

    @Schema(
        description = "Всего страниц",
        example = "5",
        minimum = "0"
    )
    val totalPages: Int,

    @Schema(
        description = "Общее количество товаров",
        example = "100",
        minimum = "0"
    )
    val totalProducts: Long
)
