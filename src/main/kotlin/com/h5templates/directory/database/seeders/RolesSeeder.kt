package com.h5templates.directory.database.seeders

import com.h5templates.directory.feature.role.entity.Role
import com.h5templates.directory.feature.role.model.RoleType
import com.h5templates.directory.feature.role.repository.RoleRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(2)
class RolesSeeder(private val roleRepository: RoleRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val existingRoles = roleRepository.findAll()
            .map { it.id }
            .toSet()

        val newRoles = RoleType.values().asSequence()
            .filterNot { existingRoles.contains(it.id) }
            .map { Role(id = it.id, name = it.name) }
            .toList()

        if (newRoles.isNotEmpty()) {
            roleRepository.saveAll(newRoles)
        }
    }
}