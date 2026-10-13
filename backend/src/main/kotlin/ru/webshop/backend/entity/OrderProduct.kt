package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "order_product")
data class OrderProduct(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "quantity")
    val quantity: Long = 1,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    val order: Order,
)
