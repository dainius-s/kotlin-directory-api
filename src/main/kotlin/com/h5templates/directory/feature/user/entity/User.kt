package com.h5templates.directory.user.entity
import com.fasterxml.jackson.annotation.JsonIgnore
import com.h5templates.directory.feature.role.entity.Role
import com.h5templates.directory.user.model.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override val id: Int = 0,

    @Column(nullable = false, length = 120)
    val name: String,

    @Column(nullable = false, length = 250, unique = true)
    val email: String,

    @Column(nullable = false, length = 13, unique = true)
    val phone: String,

    @Column(nullable = false, length = 250)
    @JsonIgnore
    val password: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    val role: Role,

    @Column(nullable = false)
    val verified: Boolean = false,

    @Column(nullable = false)
    val active: Boolean = false,
): BaseEntity(id)