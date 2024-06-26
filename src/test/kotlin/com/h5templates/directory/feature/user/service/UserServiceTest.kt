package com.h5templates.directory.feature.user.service

import com.h5templates.directory.feature.role.model.RoleType
import com.h5templates.directory.feature.role.service.RoleService
import com.h5templates.directory.user.entity.User
import com.h5templates.directory.user.model.BaseEntityDTO
import com.h5templates.directory.user.repository.UserRepository
import com.h5templates.directory.user.model.CreateUserDTO
import com.h5templates.directory.user.model.UpdateUserDTO
import com.h5templates.directory.user.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class UserServiceTest {

    private val dataSource: UserRepository = mockk(relaxed = true)
    private val roleService: RoleService = mockk(relaxed = true)
    private val passwordEncoder: PasswordEncoder = mockk(relaxed = true)
    private val userService = UserService(dataSource, roleService, passwordEncoder)

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
        val roleId = RoleType.SUPER_ADMIN.id
        val expectedUser = User(
            id,
            "Joe Biederman",
            "joe.biederman@example.com",
            "07722000001",
            "labas789",
            roleService.getRole(roleId),
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
        val roleId = RoleType.SUPER_ADMIN.id
        val payload = CreateUserDTO(
            "Joe Biederman",
            "joe.biederman@example.com",
            "07722000001",
            "labas789",
            "labas789",
            BaseEntityDTO(roleId),
            false,
            true,
        )
        val createdUser = User(
            1,
            payload.name,
            payload.email,
            payload.phone,
            payload.password,
            roleService.getRole(roleId),
            payload.verified,
            payload.active,
        )
        val userSlot = slot<User>()
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
        val roleId = RoleType.SUPER_ADMIN.id
        val existingUser = User(
            id,
            "Existing Name",
            "existing@example.com",
            "07722000001",
            "labas789",
            roleService.getRole(roleId),
            true,
            true,
        )
        val updatedUser = User(
            id,
            "Joe Biederman",
            "joe.biederman@example.com",
            "07722000002",
            "labas789",
            roleService.getRole(roleId),
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
            "07722000002",
            BaseEntityDTO(roleId),
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
        val roleId = RoleType.SUPER_ADMIN.id
        val existingUser = User(
            id,
            "Existing Name",
            "existing@example.com",
            "07722000001",
            "labas789",
            roleService.getRole(roleId),
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