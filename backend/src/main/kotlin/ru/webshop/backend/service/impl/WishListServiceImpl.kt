package ru.webshop.backend.service.impl

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.webshop.backend.dto.WishListItemDTO
import ru.webshop.backend.dto.WishListPageResponseDTO
import ru.webshop.backend.entity.CartItem
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.User
import ru.webshop.backend.entity.WishListItem
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.UserRepository
import ru.webshop.backend.repository.WishListItemRepository
import ru.webshop.backend.service.interfaces.WishListService

@Service
class WishListServiceImpl(
    private val wishListItemRepository: WishListItemRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
) : WishListService {

    @Transactional
    override fun addToWishList(telegramId: Long, articleNumber: Long) : WishListItemDTO{
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")

        if (wishListItemRepository.findByUserAndProductArticleNumber(user, articleNumber) == null) {
            val item = WishListItem(product = product, user = user)
            return wishListItemRepository.save(item).toDTO()
        }else{
            throw IllegalStateException("WishListService can only be added to a product")
        }
    }

    @Transactional
    override fun removeFromWishList(telegramId: Long, articleNumber: Long) : WishListItemDTO{
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")
        val wishListItem: WishListItem = wishListItemRepository.findByUserAndProductArticleNumber(user, articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")

        wishListItem.let { wishListItemRepository.deleteByUserAndProductArticleNumber(user, articleNumber) }
        wishListItemRepository.deleteByUserAndProductArticleNumber(user, articleNumber)

        return wishListItem.toDTO()
    }

    override fun getWishList(telegramId: Long, pageable: Pageable): WishListPageResponseDTO {
        val user: User = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val page = wishListItemRepository.findAllByUser(user, pageable)
        return WishListPageResponseDTO(
            items = page.content.map { it.toDTO() },
            currentPage = page.number,
            totalPages = page.totalPages,
            totalItems = page.totalElements
        )
    }

    override fun productInWishList(telegramId: Long, articleNumber: Long): Boolean{
        val user = userRepository.findByTelegramId(telegramId)
            ?: throw GlobalExceptionHandler.UserNotFoundException("User with telegram id $telegramId not found")
        val wlitem = wishListItemRepository.findByUserAndProductArticleNumber(user, articleNumber)
            ?: return false
        return true
    }

    private fun WishListItem.toDTO() = WishListItemDTO(
        id = id,
        articleNumber = product.articleNumber,
        productName = product.name,
        price = product.price,
        addDate = addDate
    )
}