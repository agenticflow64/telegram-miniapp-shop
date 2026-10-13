package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PhotoReviewRequestDTO(
    @Schema(
        description = "ID отзыва",
        example = "42",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val reviewId: Long,

    @Schema(
        description = "Порядковый номер фото (от 1 до 5)",
        example = "1",
        minimum = "1",
        maximum = "5"
    )
    val indexNumber: Int
)
