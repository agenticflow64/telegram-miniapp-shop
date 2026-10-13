package ru.webshop.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.webshop.backend.entity.Product

interface ProductRepository : JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = [
        "category",
        "values",
        "values.attribute",
    ])
    override fun findAll(spec: Specification<Product>?, pageable: Pageable): Page<Product>

    fun findByArticleNumber(articleNumber: Long): Product?

    fun existsByArticleNumber(articleNumber: Long): Boolean

    fun deleteByArticleNumber(articleNumber: Long)
}