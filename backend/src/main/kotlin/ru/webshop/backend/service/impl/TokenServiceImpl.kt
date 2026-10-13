package ru.webshop.backend.service.impl

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import ru.webshop.backend.config.JwtConfig
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.entity.Token
import ru.webshop.backend.entity.User
import ru.webshop.backend.enums.CookieName
import ru.webshop.backend.enums.TokenType
import ru.webshop.backend.exception.GlobalExceptionHandler
import ru.webshop.backend.repository.TokenRepository
import ru.webshop.backend.service.JwtService
import ru.webshop.backend.service.interfaces.TokenService
import ru.webshop.backend.service.interfaces.UserService
import java.time.LocalDateTime

@Service
class TokenServiceImpl(
    private val tokenRepository: TokenRepository,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val jwtConfig: JwtConfig,
) : TokenService {

    override fun saveRefreshToken(user: User, refreshToken: String) {
        val token = Token(
            user = user,
            token = refreshToken,
            tokenType = TokenType.REFRESH.name,
            expired = false,
            revoked = false
        )
        tokenRepository.save(token)
    }

    override fun revokeRefreshToken(token: String) {
        val storedToken = tokenRepository.findByToken(token)
        storedToken?.let {
            it.revoked = true
            it.expired = true
            tokenRepository.save(it)
        }
    }

    override fun findRefreshToken(token: String): Token? = tokenRepository.findByToken(token)

    override fun revokeAllRefreshTokens(user: User) {
        val tokens = tokenRepository.findValidRefreshTokensByUser(user.id)
        tokens.forEach {
            it.revoked = true
            it.expired = true
        }
        tokenRepository.saveAll(tokens)
    }

    override fun refreshToken(refreshToken: String, response: HttpServletResponse): AuthResponseDTO {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw GlobalExceptionHandler.InvalidTokenException("Invalid or expired refresh token")
        }

        val userTelegramId: String = jwtService.extractTelegramId(refreshToken) // Поменять на TG ID
            ?: throw GlobalExceptionHandler.InvalidTokenException("Invalid refresh token")

        val user: User = userService.findByTelegramId(userTelegramId.toLong()) // Поменять на TG ID
            ?: throw GlobalExceptionHandler.InvalidTokenException("User not found")

        revokeRefreshToken(refreshToken)
        val newRefreshToken: String = jwtService.generateRefreshToken(user)
        saveRefreshToken(user, newRefreshToken)

        val newAccessToken: String = jwtService.generateAccessToken(user)

        response.addCookie(jwtService.createHttpOnlyCookie(CookieName.ACCESS_TOKEN.name, newAccessToken))
        response.addCookie(jwtService.createHttpOnlyCookie(CookieName.REFRESH_TOKEN.name, newRefreshToken))

        return AuthResponseDTO(
            accessToken = newAccessToken,
            issuedAt = LocalDateTime.now(),
            accessExpiresAt = LocalDateTime.now().plusSeconds(jwtConfig.expiration),
            refreshToken = newRefreshToken,
            refreshExpiresAt = LocalDateTime.now().plusSeconds(jwtConfig.refreshExpiration)
        )
    }
}
