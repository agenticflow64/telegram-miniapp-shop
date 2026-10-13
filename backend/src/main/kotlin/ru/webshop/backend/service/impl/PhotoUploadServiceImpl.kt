package ru.webshop.backend.service.impl

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.entity.Photo
import ru.webshop.backend.entity.PhotoProduct
import ru.webshop.backend.entity.PhotoReview
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.*
import ru.webshop.backend.service.interfaces.PhotoStorageService
import ru.webshop.backend.service.interfaces.PhotoUploadService
import java.util.*

@Service
class PhotoUploadServiceImpl(
    private val productRepository: ProductRepository,
    private val photoStorageService: PhotoStorageService,
    private val photoRepository: PhotoRepository,
    private val photoProductRepository: PhotoProductRepository,
    private val photoReviewRepository: PhotoReviewRepository,
    private val reviewRepository: ReviewRepository,
) : PhotoUploadService {

    override fun uploadPhotoForProduct(articleNumber: Long, file: MultipartFile, indexNumber: Int): String {
        
        val product = productRepository.findByArticleNumber(articleNumber)
            ?: throw GlobalExceptionHandler.ProductNotFoundException(articleNumber.toString())

        val objectKey = "${articleNumber}/product/photo_$indexNumber"

        val photoUrl = photoStorageService.uploadPhoto(
            objectKey = objectKey,
            content = file.bytes,
            contentType = file.contentType ?: "image/jpeg"
        )

        val photo = photoRepository.save(
            Photo(
                objectKey = objectKey,
                photoUrl = photoUrl
            )
        )

        photoProductRepository.save(
            PhotoProduct(
                indexNumber = indexNumber,
                product = product,
                photo = photo
            )
        )

        return photoUrl
    }

    override fun uploadPhotoForReview(reviewId: Long, userId: Long, file: MultipartFile, indexNumber: Int): String {

        val review = reviewRepository.findReviewById(reviewId)
            ?: throw IllegalArgumentException("Review with id $reviewId not found")

        val objectKey = "${review.product.articleNumber}/review/photo_$indexNumber"

        val photoUrl = photoStorageService.uploadPhoto(
            objectKey = objectKey,
            content = file.bytes,
            contentType = file.contentType ?: "image/jpeg"
        )

        val photo = photoRepository.save(
            Photo(
                objectKey = objectKey,
                photoUrl = photoUrl
            )
        )

        photoReviewRepository.save(
            PhotoReview(
                indexNumber = indexNumber,
                photo = photo,
                review = review
            )
        )

        return photoUrl
    }
}