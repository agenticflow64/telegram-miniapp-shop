package ru.webshop.backend.service.interfaces

import ru.webshop.backend.dto.AttributeResponseDTO
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Category
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Value

interface AttributeService {
    fun createAttributeToCategory(categoryId: Long, attributeName: String): Attribute
    fun addValueToAttribute(attributeId: Long, product: Product, value: String): Value
    fun getAttributesByCategory(categoryId: Long): List<Attribute>
    fun deleteAttribute(attributeId: Long)
    fun findOrCreateAttribute(categoryId: Long, attributeName: String): Attribute
    fun getAttributeById(id: Long): Attribute?
    fun getAttributesByCategorytoDTO(categoryId: Long): AttributeResponseDTO
    fun attributeExists(categoryId: Long, name: String): Boolean
}