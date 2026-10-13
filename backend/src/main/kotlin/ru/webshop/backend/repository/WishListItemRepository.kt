package ru.webshop.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.User
import ru.webshop.backend.entity.WishListItem

interface WishListItemRepository: JpaRepository<WishListItem, Long> {
    fun findByUserAndProductArticleNumber(user: User, articleNumber: Long): WishListItem?
    fun findAllByUser(user: User, pageable: Pageable): Page<WishListItem>
    fun deleteByUserAndProductArticleNumber(user: User, articleNumber: Long)
}