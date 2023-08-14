package com.h5templates.directory.service

import com.h5templates.directory.datasource.UserDataSource
import com.h5templates.directory.model.User
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserServiceTest {

    private val dataSource: UserDataSource = mockk(relaxed = true)
    private val userService = UserService(dataSource)
    
    @Test
    fun `should call its data source to retrieve users`() {
        // when
        userService.getUsers()
        
        // then
        verify(exactly = 1) {
            dataSource.retrieveUsers()
        }
    }

    @Test
    fun `should call its data source to retrieve user with id`() {
        //given
        val id = 1

        // when
        userService.getUser(id)

        // then
        verify(exactly = 1) {
            dataSource.retrieveUser(id)
        }
    }

    @Test
    fun `should call its data source to create user with data provided`() {
        //given
        val payload = User(
            10,
            "Joe Biederman",
            "joe.biederman@example.com",
            false,
            true,
        )

        // when
        userService.createUser(payload)

        // then
        verify(exactly = 1) {
            dataSource.createUser(payload)
        }
    }
}