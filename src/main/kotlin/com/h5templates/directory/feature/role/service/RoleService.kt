package com.h5templates.directory.feature.role.service

import com.h5templates.directory.feature.role.entity.Role
import com.h5templates.directory.feature.role.repository.RoleRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleService(
    private val dataSource: RoleRepository,
) {
    fun getRoles(): Iterable<Role> = dataSource.findAll()
    fun getRole(id: Int): Role = findById(id)

    private fun findById(id: Int): Role = dataSource
        .findById(id)
        .orElseThrow { throw NoSuchElementException("Role with id: $id could not be found") }
}