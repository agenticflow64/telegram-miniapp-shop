package ru.webshop.backend.dto.admin

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import ru.webshop.backend.enums.OrderStatus

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderStatusAnalyticsDTO(
    @Schema(
        description = "Статистика по статусам заказов: статус → количество",
        example = "{\"IN_PROGRESS\":45,\"PAID\":32,\"CANCELLED\":8}"
    )
    val statusCounts: Map<OrderStatus, Long>
)
