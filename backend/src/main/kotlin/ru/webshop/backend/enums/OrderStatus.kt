package ru.webshop.backend.enums

import io.swagger.v3.oas.annotations.media.Schema

enum class OrderStatus {
    @Schema(description = "Ожидание оплаты")
    WAITING_FOR_PAYMENT,

    @Schema(description = "Оплачен")
    PAID,

    @Schema(description = "В обработке")
    IN_PROGRESS,

    @Schema(description = "В пути")
    IN_WAY,

    @Schema(description = "Отменен")
    CANCELLED
}