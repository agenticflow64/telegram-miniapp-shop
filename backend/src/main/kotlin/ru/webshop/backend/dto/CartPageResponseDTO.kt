package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CartPageResponseDTO(
    @Schema(description = "Список товаров в корзине")
    val items: List<CartItemDTO>,

    @Schema(description = "Текущая страница", example = "1")
    val currentPage: Int,

    @Schema(description = "Всего страниц", example = "3")
    val totalPages: Int,

    @Schema(description = "Общее количество товаров", example = "25")
    val totalItems: Long
)