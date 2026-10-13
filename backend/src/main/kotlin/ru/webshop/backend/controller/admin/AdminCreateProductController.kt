package ru.webshop.backend.controller.admin

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.dto.PhotoUploadRequestDTO
import ru.webshop.backend.dto.ProductCreateRequestDTO
import ru.webshop.backend.service.interfaces.PhotoUploadService
import ru.webshop.backend.service.interfaces.ProductService

@RestController
@RequestMapping("/api/admin/products")
class AdminCreateProductController(
    private val photoUploadService: PhotoUploadService,
    private val productService: ProductService,
) {
    @Operation(summary = "Создать товар",
        description = "Создать товар с указанными атрибутами и значениями и категорией (id)")
    @PostMapping("/create")
    fun createProduct(@RequestBody request: ProductCreateRequestDTO)
            = productService.createProduct(request)

    @Operation(summary = "Загрузить фото товара",
        description = "Загрузка фото товара по его артикулу + порядок")
    @PostMapping("/uploadPhoto", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @ModelAttribute request: PhotoUploadRequestDTO,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        return ResponseEntity.ok(
            photoUploadService.uploadPhotoForProduct(
                articleNumber = request.articleNumber,
                file = file,
                indexNumber = request.indexNumber
            )
        )
    }
}