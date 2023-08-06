package com.h5templates.directory.datasource.mock

import com.h5templates.directory.datasource.UserDataSource
import com.h5templates.directory.model.User
import org.springframework.stereotype.Repository

@Repository
class MockUserDataSource: UserDataSource {

    val users = listOf<User>(
        User(1, "John Smith", "john.smith@example.com"),
    )
    override fun retrieveUsers(): Collection<User> = users
}