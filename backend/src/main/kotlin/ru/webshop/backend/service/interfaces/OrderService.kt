package ru.webshop.backend.service.interfaces

import org.springframework.data.domain.Pageable
import ru.webshop.backend.dto.*
import ru.webshop.backend.entity.CartItem
import ru.webshop.backend.entity.User

interface OrderService {
    fun createOrder(telegramId: Long, request: CreateOrderRequestDTO): OrderDTO
    fun cancelOrder(telegramId: Long, orderId: Long): OrderDTO
    fun getOrders(telegramId: Long, pageable: Pageable): OrderPageResponseDTO
}