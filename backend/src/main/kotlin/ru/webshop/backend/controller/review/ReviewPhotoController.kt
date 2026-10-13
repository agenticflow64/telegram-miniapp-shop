package ru.webshop.backend.controller.review

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.webshop.backend.dto.PhotoReviewRequestDTO
import ru.webshop.backend.service.interfaces.PhotoUploadService

@RestController
@RequestMapping("/api/review/photo")
@Tag(name = "Photo")
class ReviewPhotoController(
    private val photoUploadService: PhotoUploadService
) {
    @Operation(summary = "Загрузить фото к отзыву",
        description = "Загружает фото к отзыву по его id + порядок")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadPhoto(
        @RequestHeader("X-Telegram-User-Id") userId: Long,
        @ModelAttribute request: PhotoReviewRequestDTO,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        return ResponseEntity.ok(
            photoUploadService.uploadPhotoForReview(
                reviewId = request.reviewId,
                userId = userId,
                file = file,
                indexNumber = request.indexNumber
            )
        )
    }
}