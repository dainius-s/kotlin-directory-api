package com.h5templates.directory.feature.user.entity
import com.fasterxml.jackson.annotation.JsonIgnore
import com.h5templates.directory.shared.model.AbstractModel
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

    @Column(nullable = false)
    val verified: Boolean = false,

    @Column(nullable = false)
    val active: Boolean = false
): AbstractModel