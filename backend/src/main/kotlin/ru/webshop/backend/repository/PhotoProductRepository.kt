package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.PhotoProduct
import ru.webshop.backend.entity.Product

interface PhotoProductRepository: JpaRepository<PhotoProduct, Long> {
    fun findByProductAndIndexNumber(product: Product, indexNumber: Int): Any
}