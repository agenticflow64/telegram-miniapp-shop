package ru.webshop.backend.controller.review

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.ReviewDTO
import ru.webshop.backend.dto.ReviewResponseDTO
import ru.webshop.backend.service.interfaces.ReviewService

@RestController
@RequestMapping("/api/review")
@Tag(name = "Review", description = "Управление отзывами")
class ReviewController(
    private val reviewService: ReviewService,
) {
    @Operation(summary = "Создание отзыва",
        description = "Отправить отзыв от пользователя к товару")
    @PostMapping
    fun uploadReview(
        @RequestHeader("X-Telegram-User-Id") userId: Long,
        @RequestBody request: ReviewDTO,
        @RequestParam("article") articleNumber : Long
    ) : ReviewResponseDTO {
        return reviewService.createReview(
            reviewDTO = request,
            userId = userId,
            productArticle = articleNumber
        )
    }

}