package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository: JpaRepository<User, Long> {
    @Query("""
        SELECT u FROM User u 
        LEFT JOIN FETCH u.roles 
        WHERE u.telegramId = :telegramId
    """)
    fun findByTelegramId(@Param("telegramId") telegramId: Long): User?

    @Query("""
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END 
        FROM User u 
        WHERE u.telegramId = :telegramId
    """)
    fun existsByTelegramId(@Param("telegramId") telegramId: Long): Boolean

    fun existsByLogin(login: String): Boolean

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login = :login")
    fun findByLogin(@Param("login") login: String): User?
}