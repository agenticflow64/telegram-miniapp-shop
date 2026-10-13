package ru.webshop.backend.filters

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import ru.webshop.backend.entity.User
import java.util.*

@Component
class TelegramHeaderFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication?.principal is User) {
            val user = authentication.principal as User
            val wrappedRequest = object : HttpServletRequestWrapper(request) {
                override fun getHeader(name: String) = when (name.lowercase()) {
                    "x-telegram-user-id" -> user.telegramId.toString()
                    else -> super.getHeader(name)
                }

                override fun getHeaders(name: String): Enumeration<String> {
                    return if (name.equals("x-telegram-user-id", ignoreCase = true)) {
                        Collections.enumeration(listOf(user.telegramId.toString()))
                    } else {
                        super.getHeaders(name)
                    }
                }

                override fun getHeaderNames(): Enumeration<String> {
                    val original = super.getHeaderNames().toList()
                    return Collections.enumeration(original + "X-Telegram-User-Id")
                }
            }
            chain.doFilter(wrappedRequest, response)
        } else {
            chain.doFilter(request, response)
        }
    }
}