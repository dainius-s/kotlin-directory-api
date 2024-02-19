package com.h5templates.directory.feature.user.repository

import com.h5templates.directory.feature.user.entity.User

interface UserDataSource {
    fun retrieveUsers(): Collection<User>
    fun retrieveUser(id: Int): User
    fun createUser(user: User): User
    fun updateUser(id: Int, user: User): User
    fun deleteUser(id: Int): Unit
}