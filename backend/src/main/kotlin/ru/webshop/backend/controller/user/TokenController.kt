package ru.webshop.backend.controller.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.service.interfaces.TokenService

@RequestMapping("/api/auth/token")
@RestController
@Tag(name = "Security")
class TokenController(
    private val tokenService: TokenService
) {
    @Operation(summary = "Обновить токен",
        description = "Обновляет токен, рефреш берется из Cookies")
    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue("REFRESH_TOKEN") refreshToken: String,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponseDTO> {
        val newTokens = tokenService.refreshToken(refreshToken, response)
        return ResponseEntity.ok(newTokens)
    }
}