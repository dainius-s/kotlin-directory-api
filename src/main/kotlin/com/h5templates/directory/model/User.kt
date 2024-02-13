package com.h5templates.directory.model
import com.h5templates.directory.shared.AbstractModel
import jakarta.persistence.*


@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override val id: Int = 0,

    @Column(nullable = false, length = 120)
    val name: String,

    @Column(nullable = false, length = 120, unique = true)
    val email: String,

    @Column(nullable = false)
    val verified: Boolean = false,

    @Column(nullable = false)
    val active: Boolean = false
): AbstractModel