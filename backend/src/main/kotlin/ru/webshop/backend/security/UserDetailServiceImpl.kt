package ru.webshop.backend.security

import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.webshop.backend.repository.UserRepository

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = when {
            username.all { it.isDigit() } ->
                userRepository.findByTelegramId(username.toLong())
            else ->
                userRepository.findByLogin(username)
        } ?: throw UsernameNotFoundException("User $username not found")

        return user.apply {
            println("Loaded user: ${this.login ?: this.telegramId} with roles: ${this.roles.joinToString { it.roleName }}")
        }
    }
}