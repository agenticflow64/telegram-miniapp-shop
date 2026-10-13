package ru.webshop.backend.service.interfaces

import jakarta.servlet.http.HttpServletResponse
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.entity.Token
import ru.webshop.backend.entity.User

interface TokenService {
    fun saveRefreshToken(user: User, refreshToken: String)
    fun revokeRefreshToken(token: String)
    fun findRefreshToken(token: String): Token?
    fun revokeAllRefreshTokens(user: User)
    fun refreshToken(refreshToken: String, response: HttpServletResponse): AuthResponseDTO
}