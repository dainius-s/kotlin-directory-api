package com.h5templates.directory.database.seeders

import com.h5templates.directory.feature.permission.repository.PermissionRepository
import com.h5templates.directory.feature.role.model.RoleType
import com.h5templates.directory.feature.role.repository.RoleRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(3)
class RolePermissionsSeeder(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        createServiceProvider()
        createAdmin()
        createSuperAdmin()
    }

    private fun createServiceProvider() {
        val role = roleRepository.findById(RoleType.SERVICE_PROVIDER.id).orElse(null)
        val permissions = permissionRepository.findByNameIn(
            listOf(
                "organisations get_self",
                "organisations create_self",
                "organisations update_self",
                "services search",
                "services get_self",
                "services create_self",
                "services update_self",
                "services delete_self",
                "dashboard provider"
            )
        )
        // Assuming Role has a method to set permissions
        role?.permissions = permissions.toSet()
        role?.let { roleRepository.save(it) }
    }

    private fun createAdmin() {
        val role = roleRepository.findById(RoleType.ADMIN.id).orElse(null)
        val permissions = permissionRepository.findByNameIn(
            listOf(
                // users permissions
                "users create_Service-Provider",
                "users create_Admin",
                "users search",
                "users get_any",
                "users update",
                "users activate",
                "users deactivate",
                // organisations permissions
                "organisations get_any",
                "organisations search",
                "organisations create",
                "organisations update",
                "organisations delete",
                "organisations verify",
                // services permissions
                "services search",
                "services get_any",
                "services create",
                "services update",
                "services delete",
                // logs permissions
                "logs search",
                // dashboard permissions
                "dashboard admin"
            )
        )
        role?.permissions = permissions.toSet()
        role?.let { roleRepository.save(it) }
    }

    private fun createSuperAdmin() {
        val role = roleRepository.findById(RoleType.SUPER_ADMIN.id).orElse(null)
        val permissions = permissionRepository.findByNameIn(
            listOf(
                // users permissions
                "users get_any",
                "users search",
                "users create",
                "users update",
                "users delete",
                "users activate",
                "users deactivate",
                // organisations permissions
                "organisations get_any",
                "organisations search",
                "organisations create",
                "organisations update",
                "organisations delete",
                "organisations verify",
                // services permissions
                "services get_self",
                "services create_self",
                "services update_self",
                "services delete_self",
                "services get_any",
                "services create",
                "services update",
                "services delete",
                "services search",
                // categories permissions
                "categories search",
                "categories get_any",
                "categories create",
                "categories update",
                "categories delete",
                // ethnicities permissions
                "ethnicities search",
                "ethnicities get_any",
                "ethnicities create",
                "ethnicities update",
                "ethnicities delete",
                // religions permissions
                "religions search",
                "religions get_any",
                "religions create",
                "religions update",
                "religions delete",
                // pages permissions
                "pages search",
                "pages create",
                "pages update",
                "pages delete",
                // logs permissions
                "logs search",
                // dashboard permissions
                "dashboard admin",
                // database permissions
                "database export",
                "database import",
                "database update"
            )
        )
        role?.permissions = permissions.toSet()
        role?.let { roleRepository.save(it) }
    }
}
