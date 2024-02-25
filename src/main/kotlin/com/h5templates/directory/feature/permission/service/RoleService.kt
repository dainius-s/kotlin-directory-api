package com.h5templates.directory.feature.permission.service

import com.h5templates.directory.feature.permission.entity.Permission
import com.h5templates.directory.feature.permission.repository.PermissionRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PermissionService(
    private val dataSource: PermissionRepository,
) {
    fun getPermissions(): Iterable<Permission> = dataSource.findAll()
    fun getPermission(id: Int): Permission = findPermission(id)

    private fun findPermission(id: Int): Permission = dataSource
        .findById(id)
        .orElseThrow { throw NoSuchElementException("Permission with id: $id could not be found") }
}