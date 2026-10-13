package ru.webshop.backend.controller.products

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.document.ProductDocument
import ru.webshop.backend.dto.ProductPageResponseDTO
import ru.webshop.backend.service.ProductDocumentService
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductsSearch", description = "Поиск товаров")
class ProductsSearchController(
    private val productService: ProductService,
    private val productDocumentService: ProductDocumentService
) {
    @Operation(summary = "Товары по категории",
        description = "Список всех товаров из конкретной категории по её id  и сортировкой. Примеры: ?sort=price,asc&sort=rating,desc\"")
    @GetMapping("/category/{categoryId}")
    fun getByCategory(
        @PathVariable categoryId: Long,
        @PageableDefault(size = 10) pageable: Pageable
    )
            = productService.getProductsByCategory(categoryId, pageable)

    @Operation(summary = "Поиск товаров с фильтром",
        description = "Поиск товаров в конкретной категории с возможными значениями атрибутов  и сортировкой. Примеры: ?sort=price,asc&sort=rating,desc\"")
    @GetMapping("/search")
    fun searchProducts(
        @RequestParam("categoryId") categoryId: Long,
        @RequestParam(required = false)
        params: MultiValueMap<String, String>,
        @PageableDefault(size = 10) pageable: Pageable
    ): ProductPageResponseDTO {

        val attributes = params
            .filterKeys { it.startsWith("attributes[") }
            .mapKeys {
                it.key
                    .removePrefix("attributes[")
                    .removeSuffix("]")
                    .toLongOrNull()
            }
            .filterKeys { it != null }
            .mapValues { (_, values) ->
                values.filter { value -> value.isNotBlank() }
            }
            .filterValues { it.isNotEmpty() }
            .mapKeys { it.key!! }

        return productService.searchProducts(categoryId, attributes, pageable)
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