package com.h5templates.directory.user.entity
import com.fasterxml.jackson.annotation.JsonIgnore
import com.h5templates.directory.feature.role.entity.Role
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
    val active: Boolean = false,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = HashSet()
): AbstractModel()