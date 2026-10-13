package ru.webshop.backend.controller.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.AddToCartRequestDTO
import ru.webshop.backend.dto.CartItemDTO
import ru.webshop.backend.dto.CartPageResponseDTO
import ru.webshop.backend.service.interfaces.CartService

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Управление корзиной пользователя")
class CartController(
    private val cartService: CartService,
) {

    @Operation(summary = "Проверка наличия в корзине",
        description = "Проверять что товар находится у пользователя в корзине")
    @GetMapping("/checkin")
    fun checkout(
        @RequestParam("article") article: Long,
        @RequestHeader("X-Telegram-User-Id") telegramId: Long
    ): CartItemDTO{
        return cartService.productInCart(telegramId, article)
    }


    @Operation(summary = "Добавить товар в корзину",
        description = "Добавлять товар, увеличивает/уменьшает количество в корзине по артикулу и пользователю")
    @PostMapping("/add")
    fun addToCart(@RequestBody request: AddToCartRequestDTO,
                  @RequestHeader("X-Telegram-User-Id") telegramId: Long) : CartItemDTO
        = cartService.addToCart(telegramId, request)

    @Operation(summary = "Удалить товар из корзины",
        description = "Удалить товар из корзины по пользователю и артикулу товара")
    @DeleteMapping("/remove")
    fun removeFromCart(@RequestParam("article") articleNumber: Long,
                       @RequestHeader("X-Telegram-User-Id") telegramId: Long) : CartItemDTO
        = cartService.removeFromCart(telegramId, articleNumber)

    @Operation(summary = "Вся корзина",
        description = "Получить всю корзину список (пагинация)")
    @GetMapping("/list")
    fun getCart(@PageableDefault(size = 20) pageable: Pageable,
                @RequestHeader("X-Telegram-User-Id") telegramId: Long): CartPageResponseDTO
        = cartService.getCart(telegramId, pageable)
}