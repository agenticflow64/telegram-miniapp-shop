package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.Instant
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CartItemDTO(
    @Schema(description = "ID элемента корзины", example = "15")
    val id: Long,

    @Schema(
        description = "Артикул товара",
        example = "123456789",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val articleNumber: Long,

    @Schema(
        description = "Название товара",
        example = "Смартфон Xiaomi Redmi Note 12"
    )
    val productName: String,

    @Schema(
        description = "Цена за единицу товара",
        example = "19999.99",
        type = "number"
    )
    val price: BigDecimal,

    @Schema(
        description = "Количество товара",
        example = "2",
        minimum = "1"
    )
    val quantity: Int,

    @Schema(
        description = "Дата добавления в корзину",
        example = "2025-03-31T14:30:00Z",
        format = "date-time"
    )
    val addDate: Instant
)
