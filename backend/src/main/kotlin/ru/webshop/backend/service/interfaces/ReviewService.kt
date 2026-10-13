package ru.webshop.backend.service.interfaces

import ru.webshop.backend.dto.ReviewDTO
import ru.webshop.backend.dto.ReviewResponseDTO
import ru.webshop.backend.entity.Review

interface ReviewService {
    fun createReview(reviewDTO: ReviewDTO, userId: Long, productArticle: Long) : ReviewResponseDTO
}