package com.h5templates.directory.service

import com.h5templates.directory.repository.UserDataSource
import com.h5templates.directory.model.User
import com.h5templates.directory.repository.UserRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest

class UserServiceTest {

    private val dataSource: UserRepository = mockk(relaxed = true)
    private val userService = UserService(dataSource)
    
    @Test
    fun `should call its data source to retrieve users`() {
        //given
        val pageable = PageRequest.of(0, 1);

        // when
        userService.getUsers(pageable)
        
        // then
        verify(exactly = 1) {
            dataSource.findAll(pageable)
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
            dataSource.findById(id)
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
            dataSource.save(payload)
        }
    }
    
    @Test
    fun `should call its data source to update user with data provided`() {
        // given
        val id = 1
        val payload = User(
            id,
            "Joe Biederman",
            "joe.biederman@example.com",
            false,
            true,
        )
        
        // when
        userService.updateUser(id, payload)
        
        // then
        verify(exactly = 1) {
            dataSource.save(payload)
        }
        
    }

    @Test
    fun `should call it's data source to delete user with id`() {
        // given
        val id = 1

        // when
        userService.deleteUser(id)

        // then
        verify(exactly = 1) {
            dataSource.deleteById(id)
        }
    }
}