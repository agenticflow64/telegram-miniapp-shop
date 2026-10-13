package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.webshop.backend.dto.ReviewDTO
import ru.webshop.backend.dto.ReviewResponseDTO
import ru.webshop.backend.entity.Product
import ru.webshop.backend.entity.Review
import ru.webshop.backend.repository.ProductRepository
import ru.webshop.backend.repository.ReviewRepository
import ru.webshop.backend.repository.UserRepository
import ru.webshop.backend.service.interfaces.ReviewService
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ReviewService {

    @Transactional
    override fun createReview(reviewDTO: ReviewDTO, userId: Long, productArticle: Long): ReviewResponseDTO {

        val product = productRepository.findByArticleNumber(productArticle)
            ?: throw IllegalArgumentException("Product with article $productArticle not found")

        val user = userRepository.findByTelegramId(userId)
            ?: throw IllegalArgumentException("User with ID $userId not found")

        val newReview = Review(
            rating = reviewDTO.rating.coerceIn(1, 5),
            reviewText = reviewDTO.text,
            product = product,
            user = user
        ).apply {
            reviewRepository.save(this)
        }

        updateProductRating(product)

        return ReviewResponseDTO(
            id = newReview.id,
            rating = newReview.rating,
            text = newReview.reviewText
        )
    }

    private fun updateProductRating(product: Product) {
        val reviews = reviewRepository.findAllByProduct(product)

        val averageRating = reviews
            .map { it.rating }
            .average()

        product.apply {
            totalReviews = reviews.size.toLong()
            rating = BigDecimal.valueOf(averageRating).setScale(2, RoundingMode.HALF_UP)
        }.also {
            productRepository.save(it)
        }
    }
}