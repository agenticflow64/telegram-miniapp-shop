package ru.webshop.backend.controller.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.service.LogoutService

@RestController
@RequestMapping("/api/logout")
@Tag(name = "Security")
class LogoutController(
    private val logoutService: LogoutService
) {
    @Operation(summary = "Выход пользователя",
        description = "Выход пользователя, очистка куков, сброс токенов (выполнять при закрытии приложения)")
    @PostMapping()
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ): ResponseEntity<String> {
        logoutService.logout(request, response, authentication)
        return ResponseEntity.ok("Logged out successfully")
    }
}