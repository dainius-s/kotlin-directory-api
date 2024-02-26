package com.h5templates.directory.feature.permission.controller

import com.h5templates.directory.feature.permission.entity.Permission
import com.h5templates.directory.feature.permission.service.PermissionService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@Validated
@RequestMapping("/api/permissions")
class PermissionController(
    private val dataService: PermissionService
) {
    @GetMapping
    fun getPermissions(): Iterable<Permission> = dataService.getPermissions()

}