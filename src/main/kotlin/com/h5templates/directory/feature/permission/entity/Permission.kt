package com.h5templates.directory.feature.permission.entity
import com.h5templates.directory.user.model.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "permissions")
data class Permission(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override val id: Int = 0,

    @Column(nullable = false, length = 120, unique = true)
    val name: String,
): BaseEntity(id)