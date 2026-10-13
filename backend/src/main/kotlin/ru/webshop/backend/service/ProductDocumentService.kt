package ru.webshop.backend.service

import org.springframework.stereotype.Service
import ru.webshop.backend.document.ProductDocument
import ru.webshop.backend.repository.ProductDocumentRepository

@Service
class ProductDocumentService(
    private val productSearchRepository: ProductDocumentRepository
) {
    fun searchProducts(query: String): List<ProductDocument> {
        return productSearchRepository.weightedSearch(query)
    }
}