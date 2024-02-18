package com.h5templates.directory.service

import com.h5templates.directory.model.User
import com.h5templates.directory.repository.UserRepository
import com.h5templates.directory.requests.user.CreateUserDTO
import com.h5templates.directory.requests.user.UpdateUserDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import java.util.*

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
        val expectedUser = User(
            id,
            "Joe Biederman",
            "joe.biederman@example.com",
            false,
            true,
        )

        // Set up the mock to return an Optional of expectedUser when findById is called
        every { dataSource.findById(id) } returns Optional.of(expectedUser)

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
        val payload = CreateUserDTO(
            "Joe Biederman",
            "joe.biederman@example.com",
            false,
            true,
        )
        val userSlot = slot<User>()
        val createdUser = User(
            1,
            payload.name,
            payload.email,
            payload.verified,
            payload.active,
        )
        every { dataSource.save(capture(userSlot)) } returns createdUser

        // when
        userService.createUser(payload)

        // then
        verify(exactly = 1) {
            dataSource.save(capture(userSlot))
        }

        Assertions.assertThat(userSlot.captured).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(createdUser)
    }

    @Test
    fun `should call its data source to update user with data provided`() {
        // given
        val id = 1
        val existingUser = User(
            id,
            "Existing Name",
            "existing@example.com",
            true,
            true,
        )
        val updatedUser = User(
            id,
            "Joe Biederman",
            "joe.biederman@example.com",
            false,
            true
        )

        // Setup mocks
        every { dataSource.findById(id) } returns Optional.of(existingUser)
        every { dataSource.save(any<User>()) } returns updatedUser // Ensure the return type is User
        val userSlot = slot<User>()

        val payload = UpdateUserDTO(
            null,
            "Joe Biederman",
            "joe.biederman@example.com",
            false,
            true,
        )

        // when
        userService.updateUser(id, payload)

        // then
        verify(exactly = 1) {
            dataSource.save(capture(userSlot))
        }

        // Here, you should individually check the fields from the captured User object
        with(userSlot.captured) {
            Assertions.assertThat(this.name).isEqualTo(payload.name)
            Assertions.assertThat(this.email).isEqualTo(payload.email)
            Assertions.assertThat(this.verified).isEqualTo(payload.verified)
            Assertions.assertThat(this.active).isEqualTo(payload.active)
        }
    }

    @Test
    fun `should call it's data source to delete user with id`() {
        // given
        val id = 1
        val existingUser = User(
            id,
            "Existing Name",
            "existing@example.com",
            true,
            true,
        )
        every { dataSource.findById(id) } returns Optional.of(existingUser)
        every { dataSource.delete(any<User>()) } returns Unit // Ensure the return type is Unit
        val slot = slot<User>()

        // when
        userService.deleteUser(id)

        // then
        verify(exactly = 1) {
            dataSource.delete(capture(slot))
        }

        Assertions.assertThat(slot.captured.id).isEqualTo(id)
    }
}