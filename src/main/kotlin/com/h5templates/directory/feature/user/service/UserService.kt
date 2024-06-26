package com.h5templates.directory.user.service

import com.h5templates.directory.feature.role.service.RoleService
import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.repository.UserRepository
import com.h5templates.directory.user.model.CreateUserDTO
import com.h5templates.directory.user.model.UpdateUserDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val dataSource: UserRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder
) {
    fun getUsers(pageable: Pageable): Page<User> = dataSource.findAll(pageable)
    fun getUser(id: Int): User = findUser(id)
    fun createUser(user: CreateUserDTO): User {
        val role = roleService.getRole(user.role.id)

        val data = User(
            name = user.name,
            email = user.email,
            phone = user.phone,
            password =  passwordEncoder.encode(user.password),
            role = role,
            verified = user.verified,
            active = user.active,
        )
        return dataSource.save(data)
    }
    fun updateUser(id: Int, user: UpdateUserDTO): User = dataSource.save(with(user) {
        val role = roleService.getRole(user.role.id)
        findUser(id).copy(
            name = name,
            email = email,
            phone = phone,
            role = role,
            verified = verified,
            active = active,
        )
    })

    fun deleteUser(id: Int): Unit = dataSource.delete(findUser(id))

    fun findByEmail(email: String): User? = dataSource
        .findByEmail(email)

    private fun findUser(id: Int): User = dataSource
        .findById(id)
        .orElseThrow { throw NoSuchElementException("User with id: $id could not be found") }
}