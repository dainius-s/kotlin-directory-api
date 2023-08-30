package com.h5templates.directory.repository.mock

import com.h5templates.directory.repository.UserDataSource
import com.h5templates.directory.model.User
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository
class MockUserDataSource: UserDataSource {

    val users = mutableListOf<User>(
        User(1, "John Smith", "john.smith@example.com"),
        User(2, "Jane Smith", "jane.smith@example.com"),
    )
    override fun retrieveUsers(): Collection<User> = users

    override fun retrieveUser(id: Int): User =
        users.firstOrNull() { it.id == id }
            ?: throw NoSuchElementException("User with id: $id could not be found")

    override fun createUser(user: User): User {
        if(users.any {it.email == user.email }) {
            throw IllegalArgumentException("User with email ${user.email} already exist")
        }
        users.add(user)

        return user
    }

    override fun updateUser(id: Int, user: User): User {
        val foundUser = users.firstOrNull() { it.id == id }
            ?: throw NoSuchElementException("User with id: $id could not be found")

        users.remove(foundUser)
        users.add(user)

        return user
    }

    override fun deleteUser(id: Int) {
        val foundUser = users.firstOrNull() { it.id == id }
            ?: throw NoSuchElementException("User with id: $id could not be found")

        users.remove(foundUser)
    }
}