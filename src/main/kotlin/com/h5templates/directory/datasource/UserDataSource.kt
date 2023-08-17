package com.h5templates.directory.datasource

import com.h5templates.directory.model.User

interface UserDataSource {
    fun retrieveUsers(): Collection<User>
    fun retrieveUser(id: Int): User
    fun createUser(user: User): User
    fun updateUser(id: Int, user: User): User
}