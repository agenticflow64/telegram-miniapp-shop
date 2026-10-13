package ru.webshop.backend.enums

import io.swagger.v3.oas.annotations.media.Schema

enum class PaymentMethods {
    @Schema(description = "Оплата картой")
                          CARD,
    @Schema(description = "Наличными")
    CASH,
    @Schema(description = "Telegram-Stars")
    TELEGRAM_STARS
}