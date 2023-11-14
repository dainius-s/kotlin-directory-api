package com.h5templates.directory.model
import com.h5templates.directory.shared.AbstractModel
import jakarta.persistence.*
import jakarta.validation.constraints.*


@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @field:NotNull
    override val id: Int = 0,

    @Column(nullable = false, length = 120)
    @field:NotNull
    @field:Size(max = 120)
    val name: String,


    @Column(nullable = false, length = 120)
    @field:NotEmpty
    @field:Email
    @field:Size(max = 120)
    val email: String,

    @Column(nullable = false)
    @field:NotNull
    val verified: Boolean = false,

    @Column(nullable = false)
    @field:NotNull
    val active: Boolean = false,
): AbstractModel