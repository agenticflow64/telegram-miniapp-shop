package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductCreateRequestDTO(
    @Schema(
        description = "Название товара",
        example = "Смартфон Xiaomi Redmi Note 12",
        minLength = 3,
        maxLength = 255
    )
    val name: String,

    @Schema(
        description = "Цена товара",
        example = "19999.99",
        type = "number"
    )
    val price: BigDecimal,

    @Schema(
        description = "Количество на складе",
        example = "50",
        minimum = "0"
    )
    val balanceInStock: Long,

    @Schema(
        description = "Описание товара",
        example = "Смартфон с AMOLED-экраном 120 Гц",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    val description: String?,

    @Schema(
        description = "ID категории",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val categoryId: Long,

    @Schema(
        description = "Атрибуты в формате: ID атрибута -> значение",
        example = "{\"3\": \"Синий\", \"5\": \"256 ГБ\"}"
    )
    val attributes: Map<String, String>,

    val numberOfOrders: Long,

    val rating : BigDecimal
)

