package com.h5templates.directory.service

import com.h5templates.directory.model.User
import com.h5templates.directory.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val dataSource: UserRepository,
) {
    fun getUsers(pageable: Pageable): Page<User> = dataSource.findAll(pageable)
    fun getUser(id: Int): Optional<User> = dataSource.findById(id)
    fun createUser(user: User): User = dataSource.save(user)
    fun updateUser(id: Int, user: User): User = dataSource.save(user)
    fun deleteUser(id: Int): Unit = dataSource.deleteById(id)
}