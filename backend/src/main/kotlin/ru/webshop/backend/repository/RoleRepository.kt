package ru.webshop.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.webshop.backend.entity.Role

interface RoleRepository: JpaRepository<Role, Long> {
    fun existsByRoleName(name: String): Boolean
    fun findRoleByRoleName(name: String): Role?
}