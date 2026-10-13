package ru.webshop.backend.controller.products

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.service.interfaces.CategoryService

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category", description = "Управление категориями")
class CategoryController(
    private val categoryService: CategoryService
) {
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