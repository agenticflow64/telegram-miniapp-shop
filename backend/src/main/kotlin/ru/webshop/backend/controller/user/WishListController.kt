package ru.webshop.backend.controller.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.WishListItemDTO
import ru.webshop.backend.dto.WishListPageResponseDTO
import ru.webshop.backend.service.interfaces.WishListService

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "WishList", description = "Управление списком желаний")
class WishListController(
    private val wishListService: WishListService,
) {

    @Operation(summary = "Проверить товар в списке желаний",
        description = "Проверять есть ли товар в списке желаний")
    @GetMapping("/checkin")
    fun checkout(
        @RequestParam("article") article: Long,
        @RequestHeader("X-Telegram-User-Id") telegramId: Long
    ): ResponseEntity<Boolean> {
        return ResponseEntity(wishListService.productInWishList(article, telegramId), HttpStatus.OK)
    }

    @Operation(summary = "Добавить в список желаний",
        description = "Пользователь добавляет товар в список желаний по артикулу")
    @PostMapping("/add")
    fun addToWishList(@RequestParam("article") articleNumber: Long,
                      @RequestHeader("X-Telegram-User-Id") telegramId: Long) : WishListItemDTO
    = wishListService.addToWishList(telegramId, articleNumber)

    @Operation(summary = "Удалить из списка желаний",
        description = "Удаляет товар из списка желаний пользователя по артикулу")
    @DeleteMapping("/remove")
    fun removeFromWishList(@RequestParam("article") articleNumber: Long,
                           @RequestHeader("X-Telegram-User-Id") telegramId: Long) : WishListItemDTO
    = wishListService.removeFromWishList(telegramId, articleNumber)

    @Operation(summary = "Весь список желаний",
        description = "Возвращает весь список желаний пользователя (пагинация)")
    @GetMapping("/list")
    fun getWishList(@PageableDefault(size = 20) pageable: Pageable,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long) : WishListPageResponseDTO
    = wishListService.getWishList(telegramId, pageable)
}