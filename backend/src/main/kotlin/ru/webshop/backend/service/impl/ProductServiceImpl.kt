package ru.webshop.backend.service.impl

import org.hibernate.Hibernate
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.webshop.backend.dto.*
import ru.webshop.backend.entity.Product
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.PhotoProductRepository
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.service.IdGeneratorService
import ru.webshop.backend.service.interfaces.AttributeService
import ru.webshop.backend.service.interfaces.CategoryService
import ru.webshop.backend.service.interfaces.ProductService
import ru.webshop.backend.service.interfaces.ValueService
import ru.webshop.backend.spec.ProductSpecifications

@Service
class ProductServiceImpl (
    private val productRepository: ProductRepository,
    private val categoryService: CategoryService,
    private val attributeService: AttributeService,
    private val valueService: ValueService,
    private val idGeneratorService: IdGeneratorService,
    private val photoProductRepository: PhotoProductRepository,
): ProductService {
    private val logger = LoggerFactory.getLogger(ProductServiceImpl::class.java)
    override fun getProduct(article: Long): ProductResponseDTO {
        val product: Product = productRepository.findByArticleNumber(article)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found: $article")
        return product.toResponseDTO()
    }

    // Список всех товаров
    override fun getAllProducts(pageable: Pageable): ProductPageResponseDTO {
        return searchProductsInternal(Specification.where(null), pageable)
    }

    // Поиск товара по категории
    override fun getProductsByCategory(categoryId: Long, pageable: Pageable): ProductPageResponseDTO {
        val spec = Specification.where(ProductSpecifications.byCategory(categoryId))
        return searchProductsInternal(spec, pageable)
    }

    // Добавить товар
    override fun createProduct(request: ProductCreateRequestDTO): Long {
        logger.info(request.toString())
        val category = categoryService.getCategoryById(request.categoryId)

        val product = productRepository.save(
            Product(
                name = request.name,
                price = request.price,
                balanceInStock = request.balanceInStock,
                description = request.description ?: throw IllegalArgumentException("Description cannot be empty"),
                category = category,
                articleNumber = idGeneratorService.generateArticleNumber(),
                rating = request.rating,
                numberOfOrders = request.numberOfOrders,
            )
        )

        request.attributes.forEach { (attrName, valueStr) ->
            val attribute = attributeService.findOrCreateAttribute(
                categoryId = category.id,
                attributeName = attrName
            )

            valueService.addValueToAttribute(
                attributeId = attribute.id,
                product = product,
                value = valueStr
            )
        }

        return product.articleNumber
    }

    // Поиск товаров
    override fun searchProducts(
        categoryId: Long,
        attributes: Map<Long, List<String>>,
        pageable: Pageable
    ): ProductPageResponseDTO {
        val spec = Specification.where(ProductSpecifications.byCategory(categoryId))
            .and(ProductSpecifications.byAttributes(attributes))

        return searchProductsInternal(spec, pageable)
    }

    private fun searchProductsInternal(spec: Specification<Product>, pageable: Pageable): ProductPageResponseDTO {
        val page = productRepository.findAll(spec, pageable)
        return ProductPageResponseDTO(
            products = page.content.map { it.toResponseDTO() },
            currentPage = page.number,
            totalPages = page.totalPages,
            totalProducts = page.totalElements
        )
    }

    // Продукт в DTO
    private fun Product.toResponseDTO(): ProductResponseDTO {
        return ProductResponseDTO(
            id = id,
            articleNumber = articleNumber,
            name = name,
            price = price,
            rating = rating.toDouble(),
            numberOfOrders = numberOfOrders,
            attributes = values.map {
                ProductAttributeDTO(
                    attributeId = it.attribute.id,
                    attributeName = it.attribute.attributeName,
                    value = it.value
                )
            },
            categoryId = category.id,
            categoryName = category.categoryName,
            photos = photos.sortedBy { it.indexNumber }
                .map { it.photo.photoUrl },
            reviews = reviews.map { review ->
                ReviewDTO(
                    id = review.id,
                    rating = review.rating,
                    text = review.reviewText,
                    createdAt = review.createdAt,
                    photos = review.reviewPhotos
                        .sortedBy { it.indexNumber }
                        .map { it.photo.photoUrl },
                    telegramId = review.user.telegramId,
                )
            }
        )
    }

    override fun deleteProduct(articleNumber: Long) {
        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException("Product not found")

        if (product.orderProducts.isNotEmpty()) {
            throw IllegalStateException("Нельзя удалить товар – он уже фигурирует в заказах")
        }

        valueService.getValuesByProduct(product.id).forEach { valueService.deleteValue(it.id) }

        product.photos.forEach { photoProductRepository.delete(it)}

        productRepository.delete(product)
    }
}