package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Category
import ru.webshop.backend.entity.Value

interface AttributeRepository: JpaRepository<Attribute, Long> {
    fun existsByIdAndCategory(id: Long, category: Category): Boolean
    fun findByCategoryId(categoryId: Long): List<Attribute>
    fun findByCategoryIdAndAttributeName(categoryId: Long, attributeName: String): Attribute?
    fun findAttributeById(id: Long): Attribute?
    fun existsByAttributeNameAndCategoryId(attributeName: String, categoryId: Long): Boolean

    @Query("SELECT a FROM Attribute a LEFT JOIN FETCH a.values WHERE a.category.id = :categoryId")
    fun findByCategoryIdWithValues(@Param("categoryId") categoryId: Long): List<Attribute>
}