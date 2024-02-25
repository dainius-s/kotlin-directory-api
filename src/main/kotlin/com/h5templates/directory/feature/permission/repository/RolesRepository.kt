package com.h5templates.directory.feature.permission.repository

import com.h5templates.directory.feature.permission.entity.Permission
import org.springframework.data.repository.CrudRepository

interface PermissionRepository: CrudRepository<Permission, Int> {

}