package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderPageResponseDTO(
    @Schema(description = "Список заказов")
    val orders: List<OrderDTO>,

    @Schema(
        description = "Текущая страница",
        example = "1",
        minimum = "0"
    )
    val currentPage: Int,

    @Schema(
        description = "Всего страниц",
        example = "3",
        minimum = "0"
    )
    val totalPages: Int,

    @Schema(
        description = "Общее количество заказов",
        example = "25",
        minimum = "0"
    )
    val totalOrders: Long
)
