package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "attribute")
data class Attribute (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "attribute_name", nullable = false)
    val attributeName: String,

    // Связи

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,

    @OneToMany(mappedBy = "attribute")
    val values: List<Value> = mutableListOf()
)
