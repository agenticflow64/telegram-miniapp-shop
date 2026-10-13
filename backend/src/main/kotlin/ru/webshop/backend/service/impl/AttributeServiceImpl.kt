package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import ru.webshop.backend.dto.AttributeResponseDTO
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Value
import ru.webshop.backend.repository.AttributeRepository
import ru.webshop.backend.repository.CategoryRepository
import ru.webshop.backend.repository.ValueRepository
import ru.webshop.backend.service.interfaces.AttributeService
import kotlin.NoSuchElementException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class AttributeServiceImpl (
    private val attributeRepository: AttributeRepository,
    private val valueRepository: ValueRepository,
    private val categoryRepository: CategoryRepository
): AttributeService {
    var logger: Logger = LoggerFactory.getLogger(this::class.java)
    // Добавить атрибут в категорию
    override fun createAttributeToCategory(categoryId: Long, attributeName: String): Attribute {
        if (attributeRepository.existsByAttributeNameAndCategoryId(attributeName, categoryId)) {
            throw RuntimeException("Attribute already exists")
        }

        val category = categoryRepository.findById(categoryId)
            .orElseThrow { NoSuchElementException("Category not found") }

        val newAttribute = Attribute(
            attributeName = attributeName,
            category = category
        )
        logger.info("Creating new attribute with {}, {}", newAttribute.category.categoryName,
            newAttribute.attributeName )
        return attributeRepository.save(newAttribute)
    }

    // Добавить значение в атрибут продукта
    override fun addValueToAttribute(attributeId: Long, product: Product, value: String): Value {
        val attribute = attributeRepository.findById(attributeId)
            .orElseThrow { NoSuchElementException("Attribute not found") }

        return valueRepository.save(Value(
            value = value,
            attribute = attribute,
            product = product
        ))
    }

    // Список атрибутов для категории по id
    override fun getAttributesByCategory(categoryId: Long): List<Attribute> {
        return attributeRepository.findByCategoryId(categoryId)
    }

    // Получить список атрибутов в виде DTO
    override fun getAttributesByCategorytoDTO(categoryId: Long): AttributeResponseDTO {
        val attributesMap = attributeRepository.findAll()
            .associate { it.attributeName to it.id }

        return AttributeResponseDTO(attributesMap)
    }

    // Удалить атрибут по id
    override fun deleteAttribute(attributeId: Long) {
        val attribute = attributeRepository.findById(attributeId)
            .orElseThrow { NoSuchElementException("Attribute not found") }

        if (attribute.values.isNotEmpty()) {
            throw IllegalStateException("Cannot delete attribute with existing values")
        }

        attributeRepository.delete(attribute)
    }

    // Найти по id или создать новый если не найдено
    override fun findOrCreateAttribute(categoryId: Long, attributeName: String): Attribute {
        return attributeRepository.findByCategoryIdAndAttributeName(categoryId, attributeName)
            ?: createAttributeToCategory(categoryId, attributeName)
    }
    // Получить атрибут по его id
    override fun getAttributeById(id: Long): Attribute? {
        return attributeRepository.findAttributeById(id)
            ?: throw NoSuchElementException("Attribute not found")
    }

    override fun attributeExists(categoryId: Long, name: String): Boolean =
        attributeRepository.existsByAttributeNameAndCategoryId(name, categoryId)
}