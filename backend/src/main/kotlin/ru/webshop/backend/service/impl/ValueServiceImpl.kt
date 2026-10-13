package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Value
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.ValueRepository
import ru.webshop.backend.service.interfaces.AttributeService
import ru.webshop.backend.service.interfaces.ValueService

@Service
class ValueServiceImpl(
    private val valueRepository: ValueRepository,
    private val attributeService: AttributeService,
) : ValueService {

    override fun getValuesByAttribute(attributeId: Long): List<Value> {
        return valueRepository.findByAttributeId(attributeId)
    }

    override fun updateValue(valueId: Long, newValue: String): Value {
        val value = valueRepository.findById(valueId)
            .orElseThrow { NoSuchElementException("Value not found") }

        return valueRepository.save(value.copy(value = newValue))
    }

    override fun deleteValue(valueId: Long) {
        valueRepository.deleteById(valueId)
    }

    override fun getValuesByProduct(productId: Long): List<Value> {
        return valueRepository.findByProductId(productId)
    }

    override fun addValueToAttribute(attributeId: Long, product: Product, value: String) {
        val attribute = attributeService.getAttributeById(attributeId)
        // Тут фаршмак из .let так как там много optional TODO Поправить
        attribute?.let {
            Value(
                value = value,
                attribute = it,
                product = product
            )
            }?.let {
                valueRepository.save(
                    it
                )
            }
    }
}