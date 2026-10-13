package ru.webshop.backend.service.impl

import ru.webshop.backend.dto.CategoryAttributesResponseDTO
import org.springframework.stereotype.Service
import ru.webshop.backend.dto.AttributeDTO
import ru.webshop.backend.dto.CategoryResponseDTO
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Category
import ru.webshop.backend.repository.AttributeRepository
import ru.webshop.backend.repository.CategoryRepository
import ru.webshop.backend.service.interfaces.CategoryService

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val attributeRepository: AttributeRepository
) : CategoryService {

    // Получить список категорий как "название" : id
    override fun getCategories(): CategoryResponseDTO {
        val categoriesMap = categoryRepository.findAll()
            .associate { it.categoryName to it.id }

        return CategoryResponseDTO(categoriesMap)
    }

    // Создать новую категорию
    override fun createCategory(categoryName: String): Category {
        if (categoryRepository.existsByCategoryName(categoryName)) {
            throw RuntimeException("Category '$categoryName' already exists")
        }
        return categoryRepository.save(Category(categoryName = categoryName))
    }

    // Получить категорию по id
    override fun getCategoryById(id: Long): Category {
        return categoryRepository.findById(id)
            .orElseThrow { NoSuchElementException("Category not found with id: $id") }
    }

    // Обновить имя категории
    override fun updateCategory(categoryId: Long, newName: String): Category {
        val category = getCategoryById(categoryId)
        return categoryRepository.save(category.copy(categoryName = newName))
    }

    // Удалить существующую категорию
    override fun deleteCategory(categoryId: Long) {
        val category = getCategoryById(categoryId)
        if (category.products.isNotEmpty() || category.attributes.isNotEmpty()) {
            throw IllegalStateException("Cannot delete category non empty: $category")
        }
        categoryRepository.delete(category)
    }
    // Поиск категории по имени
    override fun getByCategoryName(name: String): Category{
        return categoryRepository.findByCategoryName(name)
            ?: throw NoSuchElementException("Category not found: $name")
    }
    // Существует ли категория с нужным именем
    override fun existsByCategoryName(name: String): Boolean{
        return categoryRepository.existsByCategoryName(name)
    }

    // Все атрибуты категории
    override fun getCategoryAttributes(categoryId: Long): CategoryAttributesResponseDTO {
        val category = getCategoryById(categoryId)
        val attributes = attributeRepository.findByCategoryIdWithValues(categoryId)

        return CategoryAttributesResponseDTO(
            categoryId = category.id,
            categoryName = category.categoryName,
            attributes = attributes.map { it.toAttributeDTO() }
        )
    }

    // Атрибуты в DTO
    private fun Attribute.toAttributeDTO(): AttributeDTO {
        val values = values.map { it.value }.distinct()
        return AttributeDTO(
            id = id,
            name = attributeName,
            values = values
        )
    }
}