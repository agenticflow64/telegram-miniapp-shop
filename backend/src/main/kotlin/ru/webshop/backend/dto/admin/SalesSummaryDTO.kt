package ru.webshop.backend.dto.admin

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SalesSummaryDTO(
    @Schema(description = "Общее количество оформленных заказов (не отменённых)", example = "120")
    val totalOrders: Long,

    @Schema(description = "Суммарное количество проданных единиц товара", example = "356")
    val totalUnitsSold: Long,

    @Schema(description = "Итоговая выручка", example = "158990.75", type = "number")
    val totalSalesAmount: BigDecimal
)