package ru.webshop.backend.service.interfaces

import jakarta.servlet.http.HttpServletResponse
import ru.webshop.backend.dto.AuthResponseDTO

interface AuthenticationService {
    fun authenticate(webAppInitData: String, response: HttpServletResponse): AuthResponseDTO
}