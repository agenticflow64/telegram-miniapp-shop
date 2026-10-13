package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.Value

interface ValueRepository: JpaRepository<Value, Long> {
    fun findByAttributeId(attributeId: Long): List<Value>
    fun findByProductId(productId: Long): List<Value>
}