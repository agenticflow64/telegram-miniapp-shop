package ru.webshop.backend.service.interfaces

import ru.webshop.backend.entity.User

interface UserService {
    fun createUser(telegramId: Long, chatId: Long, username: String?): User
    fun findByTelegramId(telegramId: Long): User?
    fun existsByTelegramId(telegramId: Long): Boolean
}