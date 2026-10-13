package ru.webshop.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.webshop.backend.filters.TelegramHeaderFilter
import ru.webshop.backend.security.JwtFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val telegramHeaderFilter: TelegramHeaderFilter,
    private val publicRoutersConfig: PublicRoutersConfig
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                publicRoutersConfig.publicUrls.forEach { url ->
                    auth.requestMatchers(url).permitAll()
                }
                auth.requestMatchers("/api/admin/auth/**").permitAll()

                auth.requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(telegramHeaderFilter, JwtFilter::class.java)

        return http.build()
    }
}