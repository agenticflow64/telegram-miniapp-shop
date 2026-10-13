package ru.webshop.backend.controller.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.admin.*
import ru.webshop.backend.service.interfaces.AdminCatalogService
import ru.webshop.backend.service.interfaces.CategoryService

@RestController
@RequestMapping("/api/admin/categories")
@Tag(name = "AdminCategories")
class AdminCategoryController(
    private val catalogService: AdminCatalogService,
    private val categoryService: CategoryService
) {
    @Operation(summary = "Создать категорию (если есть – дополнить атрибутами)")
    @PostMapping("/create-or-update")
    fun createOrUpdate(
        @RequestBody req: CategoryCreateRequestDTO
    ): ResponseEntity<CategoryOperationResponseDTO> =
        ResponseEntity.ok(catalogService.createOrUpdate(req))

    @Operation(summary = "Создать только категорию")
    @PostMapping("/only-category")
    fun createOnly(
        @RequestBody req: CategoryCreateRequestDTO
    ): ResponseEntity<CategoryOperationResponseDTO> =
        ResponseEntity.ok(catalogService.createCategoryOnly(req.categoryName))

    @Operation(summary = "Добавить атрибуты к существующей категории")
    @PostMapping("/{categoryId}/attributes")
    fun addAttrs(
        @PathVariable categoryId: Long,
        @RequestBody req: AttributesAddRequestDTO
    ): ResponseEntity<CategoryOperationResponseDTO> =
        ResponseEntity.ok(catalogService.addAttributes(categoryId, req))

    // Список всех категорий
    @Operation(summary = "Получить список категорий",
        description = "Возвращает список категорий и id категорий")
    @GetMapping()
    fun getAllCategories(
    ) = categoryService.getCategories()

    // Атрибуты категории по id
    @Operation(summary = "Получить атрибуты категории",
        description = "Получить все атрибуты и возможные значения категории по её id")
    @GetMapping("/{categoryId}")
    fun getAttributesByCategoryId(@PathVariable categoryId: Long
    ) = categoryService.getCategoryAttributes(categoryId)
}
