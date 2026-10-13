package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "product_name", nullable = false)
    var name: String,

    @Column(name = "product_price", nullable = false)
    var price: BigDecimal,

    @Column(name = "balance_in_stock", nullable = false)
    var balanceInStock: Long,

    @Column(name = "article_number", nullable = false, unique = true)
    val articleNumber: Long = 0,

    @Column(name = "rating", nullable = true)
    var rating: BigDecimal = BigDecimal.ZERO,

    @Column(name = "description", nullable = true)
    var description: String,

    @Column(name = "number_of_orders")
    var numberOfOrders: Long = 0,

    @Column(name = "total_reviews")
    var totalReviews: Long = 0,
    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val photos: List<PhotoProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val values: List<Value> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val orderProducts: List<OrderProduct> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val reviews: List<Review> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val carts: List<CartItem> = mutableListOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    val wishLists: List<WishListItem> = mutableListOf(),
    )
