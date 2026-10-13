package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.webshop.backend.dto.admin.*
import ru.webshop.backend.service.interfaces.*

@Service
class AdminCatalogServiceImpl(
    private val categoryService: CategoryService,
    private val attributeService: AttributeService
) : AdminCatalogService {

    @Transactional
    override fun createOrUpdate(req: CategoryCreateRequestDTO): CategoryOperationResponseDTO {
        val existed = categoryService.existsByCategoryName(req.categoryName)
        val category = if (existed)
            categoryService.getByCategoryName(req.categoryName)
        else
            categoryService.createCategory(req.categoryName)

        val attrs = handleAttrs(category.id, req.attributes)
        return CategoryOperationResponseDTO(category.id, !existed, attrs)
    }

    @Transactional
    override fun createCategoryOnly(name: String): CategoryOperationResponseDTO {
        val existed = categoryService.existsByCategoryName(name)
        val category = if (existed)
            categoryService.getByCategoryName(name)
        else
            categoryService.createCategory(name)

        return CategoryOperationResponseDTO(category.id, !existed, emptyList())
    }

    @Transactional
    override fun addAttributes(
        categoryId: Long,
        req: AttributesAddRequestDTO
    ): CategoryOperationResponseDTO {
        val category = categoryService.getCategoryById(categoryId)
        val attrs = handleAttrs(category.id, req.attributes)
        return CategoryOperationResponseDTO(category.id, false, attrs)
    }

    /* — внутреннее */
    private fun handleAttrs(categoryId: Long, names: List<String>): List<AttributeIdDTO> =
        names.map { name ->
            val existed = attributeService.attributeExists(categoryId, name)
            val attr = if (existed)
                attributeService.findOrCreateAttribute(categoryId, name)
            else
                attributeService.createAttributeToCategory(categoryId, name)
            AttributeIdDTO(attr.id, attr.attributeName, !existed)
        }
}
