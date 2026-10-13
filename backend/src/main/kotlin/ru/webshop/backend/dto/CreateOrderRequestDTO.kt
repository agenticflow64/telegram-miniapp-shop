package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import ru.webshop.backend.enums.PaymentMethods
import java.time.Instant
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateOrderRequestDTO(
    @Schema(
        description = "Способ оплаты",
        example = "CASH",
        implementation = PaymentMethods::class
    )
    val paymentMethod: PaymentMethods,

    @Schema(
        description = "Адрес доставки",
        example = "ул. Пушкина, д. 15, кв. 34",
        minLength = 5,
        maxLength = 255
    )
    val orderAddress: String
)
