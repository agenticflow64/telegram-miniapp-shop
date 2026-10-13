package ru.webshop.backend.security
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.jsonwebtoken.io.IOException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import ru.webshop.backend.repository.TokenRepository
import ru.webshop.backend.service.JwtService

@Service
class JwtFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val tokenRepository: TokenRepository
): OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (request.requestURI.startsWith("/swagger-ui")
            || request.requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        logger.info("Request received. URL: ${request.requestURL}, Method: ${request.method}")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No authorization header or incorrect format. Skipping token validation.")
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authHeader.substring("Bearer ".length)

        logger.info("Token received: $jwt")

        val telegramId = try {
            jwtService.extractTelegramId(jwt)
        } catch (e: Exception) {
            logger.error("Error extracting Telegram ID from JWT", e)
            null
        }

        val isTokenValid = true

        if (telegramId != null &&
            SecurityContextHolder.getContext().authentication == null) {

            val userDetails: UserDetails = userDetailsService.loadUserByUsername(telegramId)
            logger.info("User details loaded for Telegram ID: $telegramId")

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                logger.info("JWT is valid. Authenticating user.")
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource()
                    .buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
                logger.info("User authenticated successfully.")
            }else{
                logger.warn("JWT is invalid or does not match user details.")
            }
        }
        filterChain.doFilter(request, response)
    }

}