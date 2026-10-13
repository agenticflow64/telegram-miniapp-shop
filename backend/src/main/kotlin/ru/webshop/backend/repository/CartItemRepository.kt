package ru.webshop.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.CartItem
import ru.webshop.backend.entity.User

interface CartItemRepository: JpaRepository<CartItem, Long> {
    fun findByUserAndProductArticleNumber(user: User, articleNumber: Long): CartItem?
    fun findAllByUser(user: User, pageable: Pageable): Page<CartItem>
    fun findAllByUser(user: User): List<CartItem>
    fun deleteByUserAndProductArticleNumber(user: User, articleNumber: Long)
}