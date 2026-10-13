package ru.webshop.backend.dto.admin

import ru.webshop.backend.enums.OrderStatus

data class OrderStatusCountDTO(
    val status: OrderStatus,
    val cnt: Long
)
