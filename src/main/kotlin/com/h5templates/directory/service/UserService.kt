package com.h5templates.directory.service

import com.h5templates.directory.repository.UserDataSource
import com.h5templates.directory.model.User
import org.springframework.stereotype.Service

@Service
class UserService(
    private val dataSource: UserDataSource
) {
    fun getUsers(): Collection<User> = dataSource.retrieveUsers()
    fun getUser(id: Int): User = dataSource.retrieveUser(id)
    fun createUser(user: User): User = dataSource.createUser(user)
    fun updateUser(id: Int, user: User): User = dataSource.updateUser(id, user)
    fun deleteUser(id: Int): Unit = dataSource.deleteUser(id)
}