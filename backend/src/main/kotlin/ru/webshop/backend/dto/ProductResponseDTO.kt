package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductResponseDTO(
    @Schema(description = "Уникальный ID товара в системе", example = "1")
    val id: Long,

    @Schema(description = "Артикул товара", example = "123456789")
    val articleNumber: Long,

    @Schema(description = "Название товара", example = "Смартфон Xiaomi Redmi Note 12")
    val name: String,

    val numberOfOrders: Long,

    @Schema(
        description = "Цена товара",
        example = "19999.99",
        type = "number",
        format = "double"
    )
    val price: BigDecimal,

    @Schema(
        description = "Средний рейтинг товара",
        example = "4.5",
        minimum = "0",
        maximum = "5"
    )
    val rating: Double,

    @Schema(description = "Список атрибутов товара")
    val attributes: List<ProductAttributeDTO>,

    @Schema(description = "ID категории товара", example = "5")
    val categoryId: Long,

    @Schema(description = "Название категории", example = "Смартфоны")
    val categoryName: String,

    @Schema(
        description = "Ссылки на фотографии товара",
        example = "['https://storage.example.com/photo1.jpg', 'https://storage.example.com/photo2.jpg']"
    )
    val photos: List<String>,

    @Schema(description = "Список отзывов о товаре")
    val reviews: List<ReviewDTO>
)
