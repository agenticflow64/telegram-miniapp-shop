package ru.webshop.backend.dto

import io.swagger.v3.oas.annotations.media.Schema
data class ReviewResponseDTO(
    @Schema(description = "ID отзыва", example = "15")
    val id: Long,

    @Schema(
        description = "Оценка товара",
        example = "5",
        minimum = "1",
        maximum = "5"
    )
    val rating: Int,

    @Schema(
        description = "Текст отзыва",
        example = "Отличный товар! Рекомендую к покупке.",
        minLength = 10,
        maxLength = 2000
    )
    val text: String
)