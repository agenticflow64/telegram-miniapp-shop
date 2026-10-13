package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ProductAttributeDTO(
    @Schema(description = "ID атрибута", example = "3")
    val attributeId: Long,

    @Schema(description = "Название атрибута", example = "Цвет")
    val attributeName: String,

    @Schema(
        description = "Значение атрибута",
        example = "Синий"
    )
    val value: String
)
