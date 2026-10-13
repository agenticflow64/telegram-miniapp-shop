package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.Category

interface CategoryRepository: JpaRepository<Category, Long> {
    fun findByCategoryName(categoryName: String): Category?
    fun existsByCategoryName(categoryName: String): Boolean
}