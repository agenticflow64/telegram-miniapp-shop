package ru.webshop.backend.controller.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import ru.webshop.backend.dto.CreateOrderRequestDTO
import ru.webshop.backend.dto.OrderDTO
import ru.webshop.backend.dto.OrderPageResponseDTO
import ru.webshop.backend.service.interfaces.OrderService

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Управление заказами")
class OrderController(
    private val orderService: OrderService
) {

    @Operation(summary = "Создать заказ",
        description = "Создает заказ уменьшает количество заказанных товаров на складе увеличивает кол-во товаров на складе")
    @PostMapping("/create")
    fun createOrder(@RequestBody createOrderRequestDTO: CreateOrderRequestDTO,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long) : OrderDTO
        = orderService.createOrder(telegramId, createOrderRequestDTO)

    @Operation(summary = "Отмена заказа",
        description = "Отменяет заказ по его id, ставит существующий заказ в статус отмененных, возвращает кол-во товаров на складе уменьшает кол-во заказов")
    @DeleteMapping("/cancel")
    fun cancelOrder(@RequestParam("orderId") orderId: Long,
                    @RequestHeader("X-Telegram-User-Id") telegramId: Long) : OrderDTO
        = orderService.cancelOrder(telegramId, orderId)

    @Operation(summary = "Список всех заказов",
        description = "Вернет список всех заказов пользователя (в любом статусе)")
    @GetMapping("/list")
    fun getOrders(@PageableDefault(size = 20) pageable: Pageable,
        @RequestHeader("X-Telegram-User-Id") telegramId: Long) : OrderPageResponseDTO
    = orderService.getOrders(telegramId, pageable)

}