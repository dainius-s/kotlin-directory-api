package com.h5templates.directory.feature.permission.repository

import com.h5templates.directory.feature.permission.entity.Permission
import org.springframework.data.repository.CrudRepository

interface PermissionRepository: CrudRepository<Permission, Int> {
    fun findByName(name: String): Permission?
    fun existsByName(name: String): Boolean?
    fun findByNameIn(listOf: List<String>): List<Permission>
}