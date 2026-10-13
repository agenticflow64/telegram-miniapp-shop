package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "photo")
data class Photo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "object_key", nullable = false)
    val objectKey: String,

    @Column(name = "photo_url", nullable = false)
    val photoUrl: String,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    // Связи

    @OneToMany(mappedBy = "photo")
    val productPhotos: List<PhotoProduct> = mutableListOf(),

    @OneToMany(mappedBy = "photo")
    val reviewPhotos: List<PhotoReview> = mutableListOf()
)