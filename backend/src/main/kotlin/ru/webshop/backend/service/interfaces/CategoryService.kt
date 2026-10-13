package ru.webshop.backend.service.interfaces

import ru.webshop.backend.dto.CategoryAttributesResponseDTO
import ru.webshop.backend.dto.CategoryResponseDTO
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Category

interface CategoryService {
    fun getCategories(): CategoryResponseDTO
    fun createCategory(categoryName: String): Category
    fun getCategoryById(id: Long): Category
    fun updateCategory(categoryId: Long, newName: String): Category
    fun deleteCategory(categoryId: Long)
    fun getByCategoryName(name: String): Category
    fun existsByCategoryName(name: String): Boolean
    fun getCategoryAttributes(categoryId: Long): CategoryAttributesResponseDTO
}