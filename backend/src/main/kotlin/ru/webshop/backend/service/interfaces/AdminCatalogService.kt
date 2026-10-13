package ru.webshop.backend.service.interfaces

import ru.webshop.backend.dto.admin.AttributeCreateRequestDTO
import ru.webshop.backend.dto.admin.AttributesAddRequestDTO
import ru.webshop.backend.dto.admin.CategoryCreateRequestDTO
import ru.webshop.backend.dto.admin.CategoryOperationResponseDTO
import ru.webshop.backend.entity.Category

interface AdminCatalogService {
    fun createOrUpdate(req: CategoryCreateRequestDTO): CategoryOperationResponseDTO
    fun createCategoryOnly(name: String): CategoryOperationResponseDTO
    fun addAttributes(categoryId: Long, req: AttributesAddRequestDTO): CategoryOperationResponseDTO
}