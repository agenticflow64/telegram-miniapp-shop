package ru.webshop.backend.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.webshop.backend.config.JwtConfig
import ru.webshop.backend.dto.AuthResponseDTO
import ru.webshop.backend.entity.User
import ru.webshop.backend.enums.CookieName
import ru.webshop.backend.service.JwtService
import ru.webshop.backend.service.interfaces.AuthenticationService
import ru.webshop.backend.service.interfaces.TokenService
import ru.webshop.backend.service.interfaces.UserService
import ru.webshop.backend.utils.TelegramCodeUtils
import java.time.LocalDateTime

@Service
class AuthenticationServiceImpl(
    private val objectMapper: ObjectMapper = ObjectMapper().apply {
        propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
    },
    private val authenticationProvider: AuthenticationProvider,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val tokenService: TokenService,
    private val jwtConfig: JwtConfig,
    private val telegramCodeUtils: TelegramCodeUtils,
) : AuthenticationService {
    override fun authenticate(webAppInitData: String, response: HttpServletResponse): AuthResponseDTO {
        val secretKey = telegramCodeUtils.generateSecretKey()
        val params = telegramCodeUtils.parseQueryString(webAppInitData)
        val userData = params["user"] ?: throw IllegalArgumentException("User data missing")
        val hash = params["hash"] ?: throw IllegalArgumentException("Hash missing")

        if (telegramCodeUtils.validateTelegramAuth(secretKey, params, hash)) {
            val user = parseUserData(userData)
            val authUser = authenticateUser(user)

            return generateTokens(authUser, response)

        } else {
            throw SecurityException("Invalid Telegram authentication")
        }
    }

    private fun authenticateUser(userData: TelegramUser): User {

        val existingUser = userService.findByTelegramId(userData.id)

        val user = existingUser ?: userService.createUser(
            telegramId = userData.id,
            chatId = userData.id,
            username = userData.username
        )
        val authentication = authenticationProvider.authenticate(
            UsernamePasswordAuthenticationToken(user.telegramId.toString(), null)
        )

        return authentication.principal as User
    }
    private fun generateTokens(user: User, response: HttpServletResponse): AuthResponseDTO {
        val accessToken = jwtService.generateAccessToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)

        response.addCookie(jwtService.createHttpOnlyCookie(CookieName.ACCESS_TOKEN.name, accessToken))
        response.addCookie(jwtService.createHttpOnlyCookie(CookieName.REFRESH_TOKEN.name, refreshToken))

        tokenService.saveRefreshToken(user, refreshToken)

        return AuthResponseDTO(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessExpiresAt = LocalDateTime.now().plusSeconds(jwtConfig.expiration),
            refreshExpiresAt = LocalDateTime.now().plusSeconds(jwtConfig.refreshExpiration)
        )
    }

    private fun parseUserData(userJson: String): TelegramUser {
        return objectMapper.readValue(userJson, TelegramUser::class.java)
    }

    private data class TelegramUser(
        val id: Long,
        val username: String?,
        val first_name: String?,
        val last_name: String?
    )
}