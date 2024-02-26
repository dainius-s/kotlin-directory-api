package com.h5templates.directory.feature.role.entity
import com.fasterxml.jackson.annotation.JsonIgnore
import com.h5templates.directory.feature.permission.entity.Permission
import com.h5templates.directory.shared.model.AbstractModel
import com.h5templates.directory.user.entity.User
import jakarta.persistence.*


@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override val id: Int = 0,

    @Column(nullable = false, length = 120)
    val name: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    var permissions: Set<Permission> = HashSet(),

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    var users: Set<User> = HashSet()
): AbstractModel()