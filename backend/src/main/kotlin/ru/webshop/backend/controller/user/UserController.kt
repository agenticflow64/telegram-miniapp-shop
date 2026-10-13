package ru.webshop.backend.controller.user
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import ru.webshop.backend.dto.UserResponse

@RestController
@RequestMapping("/api")
@Tag(name = "User", description = "Управление пользователями")
class UserController {

    private val logger = LoggerFactory.getLogger(UserController::class.java)
    @Operation(summary = "Получить информацию о пользователе")
    @GetMapping("/me")
    fun getInfo(@RequestHeader("X-Telegram-User-Id") telegramId: Long): ResponseEntity<UserResponse> {
        logger.info("ЗАПРОС")
        logger.info(telegramId.toString())
        return ResponseEntity.ok(UserResponse(telegramId))
    }
}

