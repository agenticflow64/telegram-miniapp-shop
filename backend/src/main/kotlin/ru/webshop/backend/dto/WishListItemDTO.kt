package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.Instant
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class WishListItemDTO(
    @Schema(description = "ID элемента списка желаний", example = "15")
    val id: Long,

    @Schema(description = "Артикул товара", example = "123456789")
    val articleNumber: Long,

    @Schema(description = "Название товара", example = "Умные часы Samsung")
    val productName: String,

    @Schema(
        description = "Цена товара",
        example = "12999.99",
        type = "number"
    )
    val price: BigDecimal,

    @Schema(
        description = "Дата добавления в список",
        example = "2025-03-31T14:30:00Z",
        format = "date-time"
    )
    val addDate: Instant
)
