package com.h5templates.directory.feature.role.repository

import com.h5templates.directory.feature.role.entity.Role
import org.springframework.data.repository.CrudRepository

interface RoleRepository: CrudRepository<Role, Int> {
    fun findByName(name: String): Role?
    fun existsByName(name: String): Boolean?
}