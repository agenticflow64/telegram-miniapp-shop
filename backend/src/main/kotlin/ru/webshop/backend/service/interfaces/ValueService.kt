package ru.webshop.backend.service.interfaces

import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Value

interface ValueService {
    fun getValuesByAttribute(attributeId: Long): List<Value>
    fun updateValue(valueId: Long, newValue: String): Value
    fun deleteValue(valueId: Long)
    fun getValuesByProduct(productId: Long): List<Value>
    fun addValueToAttribute(attributeId: Long, product: Product, value: String)
}