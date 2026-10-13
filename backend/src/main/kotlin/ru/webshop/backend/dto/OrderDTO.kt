package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import ru.webshop.backend.enums.OrderStatus
import ru.webshop.backend.enums.PaymentMethods
import java.math.BigDecimal
import java.time.Instant
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderDTO(
    @Schema(description = "ID заказа", example = "42")
    val id: Long,

    @Schema(
        description = "Статус заказа",
        implementation = OrderStatus::class
    )
    val status: OrderStatus,

    @Schema(
        description = "Общая сумма заказа",
        example = "39999.98",
        type = "number"
    )
    val orderAmount: BigDecimal,

    @Schema(
        description = "Способ оплаты",
        implementation = PaymentMethods::class
    )
    val paymentMethod: PaymentMethods,

    @Schema(
        description = "Адрес доставки",
        example = "ул. Ленина, д. 15, кв. 34"
    )
    val orderAddress: String,

    @Schema(
        description = "Ожидаемая дата доставки",
        example = "2025-04-05T12:00:00Z",
        format = "date-time"
    )
    val expectedDate: Instant,

    @Schema(
        description = "Дата создания заказа",
        example = "2025-03-31T14:35:00Z",
        format = "date-time"
    )
    val createdAt: Instant,

    @Schema(description = "Список товаров в заказе")
    val items: List<OrderItemDTO>
)
