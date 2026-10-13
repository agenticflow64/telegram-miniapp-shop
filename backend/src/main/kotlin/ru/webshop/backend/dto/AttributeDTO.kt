package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AttributeDTO(
    @Schema(description = "ID атрибута", example = "5")
    val id: Long,

    @Schema(description = "Название атрибута", example = "Цвет")
    val name: String,

    @Schema(
        description = "Допустимые значения атрибута",
        example = "[\"Красный\", \"Синий\"]"
    )
    val values: List<String>
)