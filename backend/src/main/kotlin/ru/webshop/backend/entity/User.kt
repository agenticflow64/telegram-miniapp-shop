package ru.webshop.backend.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener::class)
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "login", unique = true)
    var login: String? = null,

    @Column(name = "password_hash")
    var passwordHash: String? = null,

    @Column(name = "username_telegram", length = 64, nullable = true)
    var usernameTelegram: String = "",

    @Column(name = "telegram_id", nullable = false, unique = true)
    val telegramId: Long,

    @Column(name = "chat_id", nullable = false)
    val chatId: Long,

    @Column(name = "phone_number", nullable = true, unique = true)
    val phoneNumber: String? = null,

    @Column(name = "first_name", nullable = true, unique = true)
    val firstName: String? = null,

    @Column(name = "last_name", nullable = true, unique = true)
    val lastName: String? = null,

    @Column(name = "email", nullable = true, unique = true)
    val email: String? = null,

    @Column(name = "address", nullable = true)
    val address: String? = null,

    @Column(nullable = false)
    var enabled: Boolean,

    @Column(nullable = false)
    private var accountLocked: Boolean,

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @LastModifiedDate
    @Column(name = "updated_at")
    private var updatedAt: LocalDateTime? = null,

    // Связи

        // Роли

    @ManyToMany(fetch = FetchType.LAZY)
        var roles: MutableSet<Role> = mutableSetOf(),

    @OneToMany(mappedBy = "user")
    val orders: List<Order> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    val reviews: List<Review> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val cartItems: List<CartItem> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val wishListItems: List<WishListItem> = mutableListOf(),

    // Токены

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val tokens: MutableSet<Token> = mutableSetOf(),

    ): UserDetails, Principal{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles.map { SimpleGrantedAuthority(it.roleName) }.toMutableSet()

    override fun isEnabled(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = !accountLocked

    override fun getPassword() = passwordHash

    override fun getUsername(): String = telegramId.toString()

    override fun getName(): String = telegramId.toString()
}
