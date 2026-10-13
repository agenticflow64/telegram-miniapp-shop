package ru.webshop.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Review

interface ReviewRepository: JpaRepository<Review, Long> {
    //override fun findAll(pageable: Pageable): Page<Review> ?
    fun findReviewById(id: Long): Review?
    fun findAllByProduct(product: Product): List<Review>
}