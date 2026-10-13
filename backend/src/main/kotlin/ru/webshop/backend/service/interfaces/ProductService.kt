package ru.webshop.backend.service.interfaces

import org.springframework.data.domain.Pageable
import ru.webshop.backend.dto.ProductCreateRequestDTO
import ru.webshop.backend.dto.ProductPageResponseDTO
import ru.webshop.backend.dto.ProductResponseDTO
import ru.webshop.backend.entity.Product

interface ProductService {
    fun createProduct(request: ProductCreateRequestDTO): Long

    fun getProduct(article: Long) : ProductResponseDTO

    fun searchProducts(
        categoryId: Long,
        attributes: Map<Long, List<String>>,
        pageable: Pageable
    ): ProductPageResponseDTO

    fun getAllProducts(pageable: Pageable): ProductPageResponseDTO

    fun getProductsByCategory(categoryId: Long, pageable: Pageable): ProductPageResponseDTO

    fun deleteProduct(articleNumber: Long)
}