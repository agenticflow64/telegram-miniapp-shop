package ru.webshop.backend.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.webshop.backend.dto.admin.OrderStatusCountDTO
import ru.webshop.backend.entity.Order
import ru.webshop.backend.entity.User

interface OrderRepository: JpaRepository<Order, Long> {

    @Query("select new ru.webshop.backend.dto.admin.OrderStatusCountDTO(o.status, count(o)) " +
            "from Order o group by o.status")
    fun countByStatuses(): List<OrderStatusCountDTO>

    fun findAllByUserOrderByCreatedAtDesc(user: User, pageable: Pageable): Page<Order>
    fun findByIdAndUser(id: Long, user: User): Order?
}