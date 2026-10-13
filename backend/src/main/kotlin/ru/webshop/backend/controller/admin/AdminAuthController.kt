package ru.webshop.backend.controller.admin

import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.webshop.backend.config.JwtConfig
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.entity.User
import ru.webshop.backend.service.JwtService
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/admin/auth")
class AdminAuthController(
    private val authManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val jwtConfig: JwtConfig
) {
    data class LoginRequest(val login: String, val password: String)

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest, response: HttpServletResponse): AuthResponseDTO {

        val authentication = authManager.authenticate(
            UsernamePasswordAuthenticationToken(body.login, body.password)
        )

        val user = authentication.principal as User
        val access = jwtService.generateAccessToken(user)
        val refresh = jwtService.generateRefreshToken(user)

        response.addCookie(jwtService.createHttpOnlyCookie("ACCESS_TOKEN", access))
        response.addCookie(jwtService.createHttpOnlyCookie("REFRESH_TOKEN", refresh))

        return AuthResponseDTO(
            accessToken = access,
            refreshToken = refresh,
            accessExpiresAt = LocalDateTime.now().plusSeconds(jwtConfig.expiration),
            refreshExpiresAt = LocalDateTime.now().plusSeconds(jwtConfig.refreshExpiration)
        )
    }
}
