package com.h5templates.directory.database.seeders

import com.h5templates.directory.feature.permission.entity.Permission
import com.h5templates.directory.feature.permission.repository.PermissionRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class PermissionsSeeder(private val permissionRepository: PermissionRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val permissions = listOf(
            "users create_Service-Provider",
            "users create_Admin",
            "users get_any",
            "users search",
            "users create",
            "users update",
            "users delete",
            "users activate",
            "users deactivate",

            "organisations get_self",
            "organisations create_self",
            "organisations update_self",
            "organisations get_any",
            "organisations search",
            "organisations create",
            "organisations update",
            "organisations delete",
            "organisations verify",

            "services get_self",
            "services create_self",
            "services update_self",
            "services delete_self",
            "services get_any",
            "services create",
            "services update",
            "services delete",
            "services search",

            "categories search",
            "categories get_any",
            "categories create",
            "categories update",
            "categories delete",

            "ethnicities search",
            "ethnicities get_any",
            "ethnicities create",
            "ethnicities update",
            "ethnicities delete",

            "religions search",
            "religions get_any",
            "religions create",
            "religions update",
            "religions delete",

            "pages search",
            "pages create",
            "pages update",
            "pages delete",

            "logs search",

            "dashboard provider",
            "dashboard admin",

            "database export",
            "database import",
            "database update"
        )

        val existingPermissions = permissionRepository.findAll()
            .map { it.name }
            .toSet()

        val newPermissions = permissions
            .filterNot { existingPermissions.contains(it) }
            .map { Permission(name = it) }

        // Assuming permissionRepository supports bulk operations
        if (newPermissions.isNotEmpty()) {
            permissionRepository.saveAll(newPermissions)
        }
    }
}