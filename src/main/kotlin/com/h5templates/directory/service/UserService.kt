package com.h5templates.directory.service

import com.h5templates.directory.model.User
import com.h5templates.directory.repository.UserRepository
import com.h5templates.directory.requests.user.CreateUserDTO
import com.h5templates.directory.requests.user.UpdateUserDTO
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
    fun createUser(createUserDTO: CreateUserDTO): User {
        val user = User(
            name = createUserDTO.name,
            email = createUserDTO.email,
            verified = createUserDTO.verified,
            active = createUserDTO.active,
        )
        return dataSource.save(user)
    }
    fun updateUser(id: Int, user: UpdateUserDTO): User = dataSource.save(with(user) {
        findUser(id).copy(
            name = name,
            email = email,
            verified = verified,
            active = active,
        )
    })

    fun deleteUser(id: Int): Unit = dataSource.delete(findUser(id))
    private fun findUser(id: Int): User = dataSource
        .findById(id)
        .orElseThrow { throw NoSuchElementException("User with id: $id could not be found") }
}