package ru.webshop.backend.service.interfaces

import org.springframework.data.domain.Pageable
import ru.webshop.backend.dto.AddToCartRequestDTO
import ru.webshop.backend.dto.CartItemDTO
import ru.webshop.backend.dto.CartPageResponseDTO
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.User

interface CartService {
    fun addToCart(telegramId: Long, request: AddToCartRequestDTO) : CartItemDTO
    fun removeFromCart(telegramId: Long, articleNumber: Long) : CartItemDTO
    fun getCart(telegramId: Long, pageable: Pageable): CartPageResponseDTO
    fun productInCart(telegramId: Long, articleNumber: Long): CartItemDTO
}