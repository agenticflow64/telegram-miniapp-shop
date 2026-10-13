package ru.webshop.backend.spec

import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import ru.webshop.backend.entity.Attribute
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Value

class ProductSpecifications {

    companion object {
        private val logger = LoggerFactory.getLogger(ProductSpecifications::class.java)
        fun byCategory(categoryId: Long): Specification<Product> {
            return Specification { root, _, cb ->

                logger.info("Filtering by category ID: $categoryId (Type: ${categoryId::class.java})")
                cb.equal(root.get<Product>("category").get<Long>("id"), categoryId)
            }
        }

        fun byAttributes(attributes: Map<Long, List<String>>): Specification<Product> {
            return Specification { root, query, cb ->
                logger.info("Processing attributes: $attributes")
                if (attributes.isEmpty()) {
                    logger.info("No attributes filter applied")
                    return@Specification null
                }

                val predicates = attributes
                    .filter { (_, values) -> values.isNotEmpty() }
                    .map { (attrId, values) ->
                        logger.info("Building predicate for attribute: $attrId, values: $values")

                        val subquery = query?.subquery(Long::class.javaObjectType)
                            ?: throw IllegalStateException("CriteriaQuery is null")

                        val valueRoot = subquery.from(Value::class.java)

                        subquery.select(valueRoot.get<Product>("product").get<Long>("id"))
                        subquery.where(
                            cb.and(
                                cb.equal(valueRoot.get<Attribute>("attribute").get<Long>("id"), attrId),
                                valueRoot.get<String>("value").`in`(values),
                                cb.equal(valueRoot.get<Product>("product").get<Long>("id"), root.get<Long>("id"))
                            )
                        )
                        cb.exists(subquery)
                    }

                if (predicates.isEmpty()) {
                    null
                } else {
                    cb.and(*predicates.toTypedArray())
                }
            }
        }
    }
}