package com.h5templates.directory.feature.role.controller

import com.h5templates.directory.feature.role.entity.Role
import com.h5templates.directory.feature.role.service.RoleService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@Validated
@RequestMapping("/api/roles")
class RoleController(
    private val roleService: RoleService
) {
    @GetMapping
    fun getRoles(): Iterable<Role> = roleService.getRoles()

    @GetMapping("/{id}")
    fun getRole(@PathVariable id: Int) = roleService.getRole(id)

}