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
import ru.webshop.backend.dto.ProductResponseDTO
import ru.webshop.backend.service.ProductDocumentService
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Управление товарами")
class ProductsController(
    private val productService: ProductService
) {
    @Operation(summary = "Информация о конкретном товаре",
        description = "Получение подробной информации о товаре по артикулу (отзывы, фото и т.д.)")
    @GetMapping("/{article}")
    fun getProduct(@PathVariable article: Long) : ProductResponseDTO
        = productService.getProduct(article)

    @Operation(summary = "Получить все товары",
        description = "Список всех товаров с пагинацией  и сортировкой. Примеры: ?sort=price,asc&sort=rating,desc\"")
    @GetMapping
    fun getAllProducts(
        @PageableDefault(size = 10) pageable: Pageable)
    = productService.getAllProducts(pageable)
}