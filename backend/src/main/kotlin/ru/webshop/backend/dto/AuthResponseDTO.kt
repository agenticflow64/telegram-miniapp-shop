package ru.webshop.backend.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AuthResponseDTO (
    @Schema(
        description = "JWT токен для доступа к API",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    )
    var accessToken: String,

    @Schema(
        description = "Срок действия access токена",
        example = "2025-03-31T15:30:00"
    )
    var accessExpiresAt: LocalDateTime? = null,

    @Schema(
        description = "Время выдачи токенов",
        example = "2025-03-31T13:30:00"
    )
    var issuedAt: LocalDateTime? = null,

    @Schema(
        description = "Токен для обновления access токена",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    )
    var refreshToken: String? = null,

    @Schema(
        description = "Срок действия refresh токена",
        example = "2025-04-07T13:30:00"
    )
    var refreshExpiresAt: LocalDateTime? = null,
)
