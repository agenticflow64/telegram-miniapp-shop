package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ReviewDTO(
    @Schema(description = "ID отзыва (только для ответов)", example = "15")
    val id: Long?,

    @Schema(
        description = "Оценка товара",
        example = "5",
        minimum = "1",
        maximum = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val rating: Int,

    @Schema(
        description = "Текст отзыва",
        example = "Отличный товар, рекомендую!",
        minLength = 10,
        maxLength = 2000
    )
    val text: String,

    @Schema(description = "Дата создания отзыва", example = "2025-03-31T14:30:00Z")
    val createdAt: Instant?,

    @Schema(
        description = "Ссылки на фотографии в отзыве",
        example = "['https://storage.example.com/review_photo1.jpg']"
    )
    val photos: List<String>?,

    val telegramId: Long? = null
)