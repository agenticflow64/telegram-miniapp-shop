package ru.webshop.backend.service.impl

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.webshop.backend.dto.AddToCartRequestDTO
import ru.webshop.backend.dto.CartItemDTO
import ru.webshop.backend.dto.CartPageResponseDTO
import ru.webshop.backend.entity.CartItem
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.User
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.CartItemRepository
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.UserRepository
import ru.webshop.backend.service.interfaces.CartService

@Service
class CartServiceImpl(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository,
    private val userRepository: UserRepository
): CartService {

    // Добавляет товар в корзину, если товар уже есть то прибавит количества
    // (убавить из корзины тогда можно будет передав quantity как -1)
    override fun addToCart(telegramId: Long, request: AddToCartRequestDTO): CartItemDTO {
        val user = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val product = productRepository.findByArticleNumber(request.articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")

        return cartItemRepository.findByUserAndProductArticleNumber(user, request.articleNumber)?.let { existingItem ->
            val newQuantity = existingItem.quantity + request.quantity
            if (newQuantity > 0) {
                existingItem.quantity = newQuantity
                cartItemRepository.save(existingItem)
                existingItem.toDTO()
            } else {
                cartItemRepository.delete(existingItem)
                existingItem.toDTO()
            }
        } ?: run {
            val validatedQuantity = if (request.quantity > 0) request.quantity else 1
            val newItem = CartItem(
                product = product,
                user = user,
                quantity = validatedQuantity
            )
            cartItemRepository.save(newItem)
            newItem.toDTO()
        }
    }

    // Удалить из корзины товар
    @Transactional
    override fun removeFromCart(telegramId: Long, articleNumber: Long): CartItemDTO {
        val user = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")

        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")

        val cartItem = cartItemRepository.findByUserAndProductArticleNumber(user, articleNumber)
            ?: throw IllegalStateException("CartItem not found")

        cartItemRepository.deleteByUserAndProductArticleNumber(user, articleNumber)
        return cartItem.toDTO()
    }

    override fun getCart(telegramId: Long, pageable: Pageable): CartPageResponseDTO {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val page = cartItemRepository.findAllByUser(user, pageable)
        return CartPageResponseDTO(
            items = page.content.map { it.toDTO() },
            currentPage = page.number,
            totalPages = page.totalPages,
            totalItems = page.totalElements
        )
    }

    override fun productInCart(telegramId: Long, articleNumber: Long): CartItemDTO{
        val user = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")

        return cartItemRepository.findByUserAndProductArticleNumber(user, articleNumber)?.toDTO()
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")
    }

    private fun CartItem.toDTO() = CartItemDTO(
        id = id,
        articleNumber = product.articleNumber,
        productName = product.name,
        price = product.price,
        quantity = quantity,
        addDate = addDate
    )
}