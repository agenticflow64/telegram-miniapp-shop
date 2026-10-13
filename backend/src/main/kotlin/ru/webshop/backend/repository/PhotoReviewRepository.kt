package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.PhotoReview

interface PhotoReviewRepository: JpaRepository<PhotoReview, Long> {
}