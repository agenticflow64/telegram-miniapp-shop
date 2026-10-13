package ru.webshop.backend.dto.admin

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductSalesDTO(
    @Schema(description = "Артикул товара", example = "123456789")
    val articleNumber: Long,

    @Schema(description = "Название товара", example = "Тетрадь 48 л. клетка")
    val productName: String,

    @Schema(description = "Продано штук", example = "93")
    val totalUnitsSold: Long,

    @Schema(description = "Выручка по товару", example = "2790.00", type = "number")
    val totalSalesAmount: BigDecimal
)
