package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "category_name", nullable = false)
    val categoryName: String,

    // Связи

    @OneToMany(mappedBy = "category")
    val products: List<Product> = mutableListOf(),

    @OneToMany(mappedBy = "category")
    val attributes: List<Attribute> = mutableListOf()
)
