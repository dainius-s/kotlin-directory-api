package com.h5templates.directory.feature.role.entity
import com.fasterxml.jackson.annotation.JsonIgnore
import com.h5templates.directory.feature.permission.entity.Permission
import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.model.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "roles")
data class Role(
    @Id
    override val id: Int,

    @Column(nullable = false, length = 120, unique = true)
    val name: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    var permissions: Set<Permission> = HashSet(),

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    var users: Set<User> = HashSet()
): BaseEntity(id)