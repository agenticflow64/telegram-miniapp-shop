package ru.webshop.backend.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.document.ProductDocument
import ru.webshop.backend.service.ProductDocumentService
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/admin/products")
@Tag(name = "AdminProducts", description = "CRUD-операции с товарами (админ)")
class AdminProductController(
    private val productService: ProductService,
    private val productDocumentService: ProductDocumentService
) {

    @Operation(summary = "Удалить товар по артикулу")
    @DeleteMapping("/{articleNumber}")
    fun delete(@PathVariable articleNumber: Long): ResponseEntity<Void> {
        productService.deleteProduct(articleNumber)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search/elastic")
    fun searchProducts(
        @RequestParam query: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<ProductDocument>> {
        val products = productDocumentService.searchProducts(query)
        val pagedProducts = PageImpl(
            products,
            PageRequest.of(page, size),
            products.size.toLong()
        )
        return ResponseEntity.ok(pagedProducts)
    }
}
