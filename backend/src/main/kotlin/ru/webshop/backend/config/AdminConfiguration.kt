package ru.webshop.backend.config

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import ru.webshop.backend.entity.Role
import ru.webshop.backend.entity.User
import ru.webshop.backend.repository.RoleRepository
import ru.webshop.backend.repository.UserRepository

@Configuration
class AdminConfiguration(
    private val userDetailsService: UserDetailsService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.admin.login}") private val adminLogin: String,
    @Value("\${app.admin.password}") private val adminPassword: String,
) {
    @Bean
    fun daoAuthenticationProvider(
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    @Transactional
    fun initAdmin(
    ) = CommandLineRunner {
        if (!userRepository.existsByLogin(adminLogin)) {
            val adminRole = roleRepository.findRoleByRoleName("ADMIN")
                ?: roleRepository.save(Role(roleName = "ADMIN", description = "Admin role"))
            val TECHNICAL_TELEGRAM_ID: Long = 1_000_000_000_000L
            userRepository.save(
                User(
                    telegramId = TECHNICAL_TELEGRAM_ID,
                    chatId = TECHNICAL_TELEGRAM_ID,
                    enabled = true,
                    accountLocked = false,
                    login = adminLogin,
                    passwordHash = passwordEncoder.encode(adminPassword),
                    roles = mutableSetOf(adminRole)
                )
            )
        }
    }
}