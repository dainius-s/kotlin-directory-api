package com.h5templates.directory.feature.user.service

import com.h5templates.directory.feature.user.entity.User
import com.h5templates.directory.feature.user.repository.UserRepository
import com.h5templates.directory.feature.user.model.CreateUserDTO
import com.h5templates.directory.feature.user.model.UpdateUserDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val dataSource: UserRepository,
) {
    fun getUsers(pageable: Pageable): Page<User> = dataSource.findAll(pageable)
    fun getUser(id: Int): User = findUser(id)
    fun createUser(user: User): User = dataSource.save(user)
    fun createUser(user: CreateUserDTO): User {
        val data = User(
            name = user.name,
            email = user.email,
            phone = user.phone,
            verified = user.verified,
            active = user.active,
        )
        return dataSource.save(data)
    }
    fun updateUser(id: Int, user: UpdateUserDTO): User = dataSource.save(with(user) {
        findUser(id).copy(
            name = name,
            email = email,
            phone = phone,
            verified = verified,
            active = active,
        )
    })

    fun deleteUser(id: Int): Unit = dataSource.delete(findUser(id))
    private fun findUser(id: Int): User = dataSource
        .findById(id)
        .orElseThrow { throw NoSuchElementException("User with id: $id could not be found") }
}