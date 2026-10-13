package ru.webshop.backend.service.interfaces

import org.springframework.web.multipart.MultipartFile

interface PhotoUploadService {
    fun uploadPhotoForProduct(articleNumber: Long, file: MultipartFile, indexNumber: Int): String
    fun uploadPhotoForReview(reviewId: Long, userId: Long, file: MultipartFile, indexNumber: Int): String
}